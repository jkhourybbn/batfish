package org.batfish.dataplane.ibdp;

import static org.batfish.datamodel.RoutingProtocol.ISIS_L1;
import static org.batfish.datamodel.RoutingProtocol.ISIS_L2;
import static org.batfish.datamodel.matchers.AbstractRouteMatchers.hasMetric;
import static org.batfish.datamodel.matchers.AbstractRouteMatchers.hasPrefix;
import static org.batfish.datamodel.matchers.AbstractRouteMatchers.hasProtocol;
import static org.batfish.datamodel.matchers.IsisRouteMatchers.hasDown;
import static org.batfish.datamodel.matchers.IsisRouteMatchers.isIsisRouteThat;
import static org.batfish.dataplane.ibdp.TestUtils.assertNoRoute;
import static org.batfish.dataplane.ibdp.TestUtils.assertRoute;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasKey;

import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.Sets;
import java.util.Collections;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.concurrent.atomic.AtomicInteger;
import org.batfish.common.BatfishLogger;
import org.batfish.common.util.CommonUtil;
import org.batfish.datamodel.AbstractRoute;
import org.batfish.datamodel.Configuration;
import org.batfish.datamodel.ConfigurationFormat;
import org.batfish.datamodel.Interface;
import org.batfish.datamodel.InterfaceAddress;
import org.batfish.datamodel.IsisInterfaceLevelSettings;
import org.batfish.datamodel.IsisInterfaceMode;
import org.batfish.datamodel.IsisInterfaceSettings;
import org.batfish.datamodel.IsisLevelSettings;
import org.batfish.datamodel.IsisProcess;
import org.batfish.datamodel.IsoAddress;
import org.batfish.datamodel.NetworkFactory;
import org.batfish.datamodel.Prefix;
import org.batfish.datamodel.Topology;
import org.batfish.datamodel.Vrf;
import org.junit.Test;

public class IsisTest {

  @Test
  public void testRoutes() {
    NetworkFactory nf = new NetworkFactory();
    Configuration.Builder cb =
        nf.configurationBuilder().setConfigurationFormat(ConfigurationFormat.CISCO_IOS);
    Vrf.Builder vb = nf.vrfBuilder().setName(Configuration.DEFAULT_VRF_NAME);
    IsisProcess.Builder ipb = IsisProcess.builder();
    Interface.Builder ib = nf.interfaceBuilder().setActive(true);
    IsisInterfaceSettings.Builder iib = IsisInterfaceSettings.builder().setPointToPoint(true);
    IsisInterfaceLevelSettings activeIls =
        IsisInterfaceLevelSettings.builder().setMode(IsisInterfaceMode.ACTIVE).build();
    IsisInterfaceLevelSettings passiveIls =
        IsisInterfaceLevelSettings.builder().setMode(IsisInterfaceMode.PASSIVE).build();
    IsisLevelSettings levelSettings = IsisLevelSettings.builder().build();

    // r1
    String r1Name = "r1";
    Configuration r1 = cb.setHostname(r1Name).build();
    Vrf v1 = vb.setOwner(r1).build();
    ib.setOwner(r1).setVrf(v1);
    iib.setLevel1(null);
    ipb.setVrf(v1)
        .setNetAddress(new IsoAddress("49.0001.0100.0100.1001.00"))
        .setLevel1(null)
        .setLevel2(levelSettings)
        .build();
    // r1l
    ib.setAddress(new InterfaceAddress("10.1.1.1/32"))
        .setIsis(iib.setLevel2(passiveIls).build())
        .build();
    // r1r2
    ib.setAddress(new InterfaceAddress("10.1.2.1/24"))
        .setIsis(iib.setLevel2(activeIls).build())
        .build();
    // r1r3
    ib.setAddress(new InterfaceAddress("10.1.3.1/24"))
        .setIsis(iib.setLevel2(activeIls).build())
        .build();

    // r2
    String r2Name = "r2";
    Configuration r2 = cb.setHostname(r2Name).build();
    Vrf v2 = vb.setOwner(r2).build();
    ib.setOwner(r2).setVrf(v2);
    iib.setLevel1(null);
    ipb.setVrf(v2)
        .setNetAddress(new IsoAddress("49.0001.0100.0200.2002.00"))
        .setLevel1(null)
        .setLevel2(levelSettings)
        .build();
    // r2l
    ib.setAddress(new InterfaceAddress("10.2.2.2/32"))
        .setIsis(iib.setLevel2(passiveIls).build())
        .build();
    // r2r1
    ib.setAddress(new InterfaceAddress("10.1.2.2/24"))
        .setIsis(iib.setLevel2(activeIls).build())
        .build();
    // r2r3
    ib.setAddress(new InterfaceAddress("10.2.3.2/24"))
        .setIsis(iib.setLevel2(activeIls).build())
        .build();

    // r3
    String r3Name = "r3";
    Configuration r3 = cb.setHostname(r3Name).build();
    Vrf v3 = vb.setOwner(r3).build();
    ib.setOwner(r3).setVrf(v3);
    ipb.setVrf(v3)
        .setNetAddress(new IsoAddress("49.0001.0100.0300.3003.00"))
        .setLevel1(levelSettings)
        .setLevel2(levelSettings)
        .build();
    // r3l
    ib.setAddress(new InterfaceAddress("10.3.3.3/32"))
        .setIsis(iib.setLevel1(passiveIls).setLevel2(passiveIls).build())
        .build();
    // r3r1
    ib.setAddress(new InterfaceAddress("10.1.3.3/24"))
        .setIsis(iib.setLevel1(null).setLevel2(activeIls).build())
        .build();
    // r3r2
    ib.setAddress(new InterfaceAddress("10.2.3.3/24"))
        .setIsis(iib.setLevel1(null).setLevel2(activeIls).build())
        .build();
    // r3r4
    ib.setAddress(new InterfaceAddress("10.3.4.3/24"))
        .setIsis(iib.setLevel1(activeIls).setLevel2(null).build())
        .build();
    // r3r5
    ib.setAddress(new InterfaceAddress("10.3.5.3/24"))
        .setIsis(iib.setLevel1(activeIls).setLevel2(null).build())
        .build();

    // r4
    String r4Name = "r4";
    Configuration r4 = cb.setHostname(r4Name).build();
    Vrf v4 = vb.setOwner(r4).build();
    ib.setOwner(r4).setVrf(v4);
    iib.setLevel2(null);
    ipb.setVrf(v4)
        .setNetAddress(new IsoAddress("49.0001.0100.0400.4004.00"))
        .setLevel1(levelSettings)
        .setLevel2(null)
        .build();
    // r4l
    ib.setAddress(new InterfaceAddress("10.4.4.4/32"))
        .setIsis(iib.setLevel1(passiveIls).build())
        .build();
    // r4r3
    ib.setAddress(new InterfaceAddress("10.3.4.4/24"))
        .setIsis(iib.setLevel1(activeIls).build())
        .build();
    // r4r5
    ib.setAddress(new InterfaceAddress("10.4.5.4/24"))
        .setIsis(iib.setLevel1(activeIls).build())
        .build();

    // r5
    String r5Name = "r5";
    Configuration r5 = cb.setHostname(r5Name).build();
    Vrf v5 = vb.setOwner(r5).build();
    ib.setOwner(r5).setVrf(v5);
    iib.setLevel2(null);
    ipb.setVrf(v5)
        .setNetAddress(new IsoAddress("49.0001.0100.0500.5005.00"))
        .setLevel1(levelSettings)
        .setLevel2(null)
        .build();
    // r5l
    ib.setAddress(new InterfaceAddress("10.5.5.5/32"))
        .setIsis(iib.setLevel1(passiveIls).build())
        .build();
    // r5r3
    ib.setAddress(new InterfaceAddress("10.3.5.5/24"))
        .setIsis(iib.setLevel1(activeIls).build())
        .build();
    // r5r4
    ib.setAddress(new InterfaceAddress("10.4.5.5/24"))
        .setIsis(iib.setLevel1(activeIls).build())
        .build();

    SortedMap<String, Configuration> configurations =
        ImmutableSortedMap.of(
            r1.getName(),
            r1,
            r2.getName(),
            r2,
            r3.getName(),
            r3,
            r4.getName(),
            r4,
            r5.getName(),
            r5);
    IncrementalBdpEngine engine =
        new IncrementalBdpEngine(
            new IncrementalDataPlaneSettings(),
            new BatfishLogger(BatfishLogger.LEVELSTR_OUTPUT, false),
            (s, i) -> new AtomicInteger());
    Topology topology = CommonUtil.synthesizeTopology(configurations);
    IncrementalDataPlane dp =
        (IncrementalDataPlane)
            engine.computeDataPlane(false, configurations, topology, Collections.emptySet())
                ._dataPlane;
    SortedMap<String, SortedMap<String, SortedSet<AbstractRoute>>> routes =
        IncrementalBdpEngine.getRoutes(dp);

    // r1
    assertNoRoute(routes, r1Name, Prefix.ZERO);
    assertRoute(routes, ISIS_L2, r1Name, Prefix.parse("10.2.2.2/32"), 10L);
    assertRoute(routes, ISIS_L2, r1Name, Prefix.parse("10.2.3.0/24"), 20L);
    assertRoute(routes, ISIS_L2, r1Name, Prefix.parse("10.3.3.3/32"), 10L);
    assertRoute(routes, ISIS_L2, r1Name, Prefix.parse("10.3.4.0/24"), 20L);
    assertRoute(routes, ISIS_L2, r1Name, Prefix.parse("10.3.5.0/24"), 20L);
    assertRoute(routes, ISIS_L2, r1Name, Prefix.parse("10.4.4.4/32"), 20L);
    assertRoute(routes, ISIS_L2, r1Name, Prefix.parse("10.4.5.0/24"), 30L);
    assertRoute(routes, ISIS_L2, r1Name, Prefix.parse("10.5.5.5/32"), 20L);
    /* TODO: https://github.com/batfish/batfish/issues/1703 */
    // assertRoute(routes, ISIS_L2, r1Name, Prefix.parse("10.3.3.100/32"), 10L);

    // r2
    assertNoRoute(routes, r2Name, Prefix.ZERO);
    assertRoute(routes, ISIS_L2, r2Name, Prefix.parse("10.1.1.1/32"), 10L);
    assertRoute(routes, ISIS_L2, r2Name, Prefix.parse("10.1.3.0/24"), 20L);
    assertRoute(routes, ISIS_L2, r2Name, Prefix.parse("10.3.3.3/32"), 10L);
    assertRoute(routes, ISIS_L2, r2Name, Prefix.parse("10.3.4.0/24"), 20L);
    assertRoute(routes, ISIS_L2, r2Name, Prefix.parse("10.3.5.0/24"), 20L);
    assertRoute(routes, ISIS_L2, r2Name, Prefix.parse("10.4.4.4/32"), 20L);
    assertRoute(routes, ISIS_L2, r2Name, Prefix.parse("10.4.5.0/24"), 30L);
    assertRoute(routes, ISIS_L2, r2Name, Prefix.parse("10.5.5.5/32"), 20L);
    /* TODO: https://github.com/batfish/batfish/issues/1703 */
    // assertRoute(routes, ISIS_L2, r2Name, Prefix.parse("10.3.3.100/32"), 10L);

    // r3
    assertNoRoute(routes, r3Name, Prefix.ZERO);
    assertRoute(routes, ISIS_L2, r3Name, Prefix.parse("10.1.1.1/32"), 10L);
    assertRoute(routes, ISIS_L2, r3Name, Prefix.parse("10.1.2.0/24"), 20L);
    assertRoute(routes, ISIS_L2, r3Name, Prefix.parse("10.2.2.2/32"), 10L);
    assertRoute(routes, ISIS_L1, r3Name, Prefix.parse("10.4.4.4/32"), 10L);
    assertRoute(routes, ISIS_L1, r3Name, Prefix.parse("10.4.5.0/24"), 20L);
    assertRoute(routes, ISIS_L1, r3Name, Prefix.parse("10.5.5.5/32"), 10L);

    // r4
    assertRoute(routes, ISIS_L1, r4Name, Prefix.ZERO, 10L);
    assertRoute(routes, ISIS_L1, r4Name, Prefix.parse("10.3.3.3/32"), 10L);
    assertRoute(routes, ISIS_L1, r4Name, Prefix.parse("10.3.5.0/24"), 20L);
    assertRoute(routes, ISIS_L1, r4Name, Prefix.parse("10.5.5.5/32"), 10L);
    /* TODO: https://github.com/batfish/batfish/issues/1703 */
    // assertInterAreaRoute(routes, r4Name, Prefix.parse("10.1.1.1/32"), 148L);
    // assertInterAreaRoute(routes, r4Name, Prefix.parse("10.1.2.0/24"), 158L);
    // assertInterAreaRoute(routes, r4Name, Prefix.parse("10.1.3.0/24"), 148L);
    // assertInterAreaRoute(routes, r4Name, Prefix.parse("10.2.2.2/32"), 148L);
    // assertInterAreaRoute(routes, r4Name, Prefix.parse("10.2.3.0/24"), 148L);
    // assertRoute(routes, ISIS_L1, r4Name, Prefix.parse("10.3.3.100/32"), 10L);

    // r5
    assertRoute(routes, ISIS_L1, r5Name, Prefix.ZERO, 10L);
    assertRoute(routes, ISIS_L1, r5Name, Prefix.parse("10.3.3.3/32"), 10L);
    assertRoute(routes, ISIS_L1, r5Name, Prefix.parse("10.3.4.0/24"), 20L);
    assertRoute(routes, ISIS_L1, r5Name, Prefix.parse("10.4.4.4/32"), 10L);
    /* TODO: https://github.com/batfish/batfish/issues/1703 */
    // assertInterAreaRoute(routes, r5Name, Prefix.parse("10.1.1.1/32"), 148L);
    // assertInterAreaRoute(routes, r5Name, Prefix.parse("10.1.2.0/24"), 158L);
    // assertInterAreaRoute(routes, r5Name, Prefix.parse("10.1.3.0/24"), 148L);
    // assertInterAreaRoute(routes, r5Name, Prefix.parse("10.2.2.2/32"), 148L);
    // assertInterAreaRoute(routes, r5Name, Prefix.parse("10.2.3.0/24"), 148L);
    // assertRoute(routes, ISIS_L1, r5Name, Prefix.parse("10.3.3.100/32"), 10L);

    // Assert r1/r2 have empty l1 rib
    assertThat(
        dp.getNodes()
            .get("r1")
            .getVirtualRouters()
            .get(Configuration.DEFAULT_VRF_NAME)
            ._isisL1Rib
            .getRoutes(),
        empty());
    assertThat(
        dp.getNodes()
            .get("r2")
            .getVirtualRouters()
            .get(Configuration.DEFAULT_VRF_NAME)
            ._isisL1Rib
            .getRoutes(),
        empty());

    // Assert r3 has disjoint l1/l2 ribs
    VirtualRouter r3Vr =
        dp.getNodes().get("r3").getVirtualRouters().get(Configuration.DEFAULT_VRF_NAME);
    assertThat(
        Sets.intersection(r3Vr._isisL1Rib.getRoutes(), r3Vr._isisL2Rib.getRoutes()), empty());
  }

  private static void assertInterAreaRoute(
      SortedMap<String, SortedMap<String, SortedSet<AbstractRoute>>> routesByNode,
      String hostname,
      Prefix prefix,
      long expectedCost) {
    assertThat(routesByNode, hasKey(hostname));
    SortedMap<String, SortedSet<AbstractRoute>> routesByVrf = routesByNode.get(hostname);
    assertThat(routesByVrf, hasKey(Configuration.DEFAULT_VRF_NAME));
    SortedSet<AbstractRoute> routes = routesByVrf.get(Configuration.DEFAULT_VRF_NAME);
    assertThat(routes, hasItem(hasPrefix(prefix)));
    AbstractRoute route =
        routes.stream().filter(r -> r.getNetwork().equals(prefix)).findAny().get();
    assertThat(route, hasMetric(expectedCost));
    assertThat(route, hasProtocol(ISIS_L1));
    assertThat(route, isIsisRouteThat(hasDown()));
  }
}
