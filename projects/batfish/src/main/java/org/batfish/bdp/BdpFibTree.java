package org.batfish.bdp;

import java.util.Map;
import java.util.Set;
import org.batfish.datamodel.AbstractRoute;
import org.batfish.datamodel.FibTree;
import org.batfish.datamodel.Ip;

public class BdpFibTree implements FibTree {

  private final BdpFibTreeNode _root;

  public BdpFibTree(
      Rib.RibTree tree,
      Map<AbstractRoute, Map<String, Map<Ip, Set<AbstractRoute>>>> nextHopInterfaces) {
    _root = new BdpFibTreeNode(tree._root, nextHopInterfaces);
  }

  public BdpFibTreeNode getRoot() {
    return _root;
  }
}
