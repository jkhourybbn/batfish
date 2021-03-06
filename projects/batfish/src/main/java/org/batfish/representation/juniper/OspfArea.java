package org.batfish.representation.juniper;

import java.util.SortedMap;
import java.util.TreeMap;
import org.batfish.common.util.ComparableStructure;
import org.batfish.datamodel.Prefix;
import org.batfish.datamodel.ospf.OspfAreaSummary;
import org.batfish.datamodel.ospf.StubType;

public class OspfArea extends ComparableStructure<Long> {

  private static final long serialVersionUID = 1L;

  private NssaSettings _nssaSettings;

  private StubSettings _stubSettings;

  private StubType _stubType;

  private SortedMap<Prefix, OspfAreaSummary> _summaries;

  public OspfArea(Long name) {
    super(name);
    _stubType = StubType.NONE;
    _summaries = new TreeMap<>();
  }

  public NssaSettings getNssaSettings() {
    return _nssaSettings;
  }

  public StubSettings getStubSettings() {
    return _stubSettings;
  }

  public StubType getStubType() {
    return _stubType;
  }

  public SortedMap<Prefix, OspfAreaSummary> getSummaries() {
    return _summaries;
  }

  public void setNssaSettings(NssaSettings nssaSettings) {
    _nssaSettings = nssaSettings;
  }

  public void setStubSettings(StubSettings stubSettings) {
    _stubSettings = stubSettings;
  }

  public void setStubType(StubType stubType) {
    _stubType = stubType;
  }
}
