package org.batfish.bdp;

import com.google.common.collect.ImmutableMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.batfish.datamodel.AbstractRoute;
import org.batfish.datamodel.ArpInfo;
import org.batfish.datamodel.FibTreeNode;
import org.batfish.datamodel.Ip;
import org.batfish.datamodel.Prefix;
import org.batfish.datamodel.Route;

public class BdpFibTreeNode implements FibTreeNode {

  /** nextHopInterface -> arpInfo */
  private final Map<String, ArpInfo> _arpInfo;

  private final BdpFibTreeNode _left;

  private final Prefix _prefix;

  private final BdpFibTreeNode _right;

  public BdpFibTreeNode(
      Rib.RibTreeNode node,
      Map<AbstractRoute, Map<String, Map<Ip, Set<AbstractRoute>>>> nextHopInterfaces) {
    if (node._left != null) {
      _left = new BdpFibTreeNode(node._left, nextHopInterfaces);
    } else {
      _left = null;
    }
    if (node._right != null) {
      _right = new BdpFibTreeNode(node._right, nextHopInterfaces);
    } else {
      _right = null;
    }
    _prefix = node._prefix;
    Map<String, ArpInfo.Builder> arpInfoBuilders = new HashMap<String, ArpInfo.Builder>();
    node._routes.forEach(
        route ->
            nextHopInterfaces
                .get(route)
                .forEach(
                    (nextHopInterface, byNextHopIp) -> {
                      ArpInfo.Builder builder =
                          arpInfoBuilders.computeIfAbsent(nextHopInterface, i -> ArpInfo.builder());
                      byNextHopIp
                          .keySet()
                          .forEach(
                              nextHopIp -> {
                                if (nextHopIp == Route.UNSET_ROUTE_NEXT_HOP_IP) {
                                  builder.setCanUseDstIp(true);
                                } else {
                                  builder.addNextHopIp(nextHopIp);
                                }
                              });
                    }));
    _arpInfo =
        arpInfoBuilders
            .entrySet()
            .stream()
            .collect(ImmutableMap.toImmutableMap(Entry::getKey, e -> e.getValue().build()));
  }

  public Map<String, ArpInfo> getArpInfo() {
    return _arpInfo;
  }

  public BdpFibTreeNode getLeft() {
    return _left;
  }

  public Prefix getPrefix() {
    return _prefix;
  }

  public BdpFibTreeNode getRight() {
    return _right;
  }
}
