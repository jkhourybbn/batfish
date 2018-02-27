package org.batfish.datamodel;

import com.google.common.collect.ImmutableSet;
import java.util.Set;

public class ArpInfo {

  public static class Builder {

    private boolean _canUseDstIp;

    private final ImmutableSet.Builder<Ip> _nextHopIps;

    private Builder() {
      _nextHopIps = ImmutableSet.builder();
    }

    public void addNextHopIp(Ip nextHopIp) {
      _nextHopIps.add(nextHopIp);
    }

    public ArpInfo build() {
      return new ArpInfo(_canUseDstIp, _nextHopIps.build());
    }

    public void setCanUseDstIp(boolean canUseDstIp) {
      _canUseDstIp = canUseDstIp;
    }
  }

  public static Builder builder() {
    return new Builder();
  }

  private final boolean _canUseDstIp;

  private final Set<Ip> _nextHopIps;

  private ArpInfo(boolean canUseDstIp, Set<Ip> nextHopIps) {
    _canUseDstIp = canUseDstIp;
    _nextHopIps = nextHopIps;
  }

  public boolean getCanUseDstIp() {
    return _canUseDstIp;
  }

  public Set<Ip> getNextHopIps() {
    return _nextHopIps;
  }
}
