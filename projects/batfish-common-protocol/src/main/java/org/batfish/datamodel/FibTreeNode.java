package org.batfish.datamodel;

import java.util.Map;

public interface FibTreeNode {

  Map<String, ArpInfo> getArpInfo();

  FibTreeNode getLeft();

  Prefix getPrefix();

  FibTreeNode getRight();
}
