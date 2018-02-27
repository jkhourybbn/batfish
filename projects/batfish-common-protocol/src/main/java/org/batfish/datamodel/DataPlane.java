package org.batfish.datamodel;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import org.batfish.datamodel.collections.FibRow;
import org.batfish.datamodel.collections.NodeInterfacePair;

public interface DataPlane extends Serializable {

  /** Mapping: hostname -> vrfName -> fibRows */
  Map<String, Map<String, SortedSet<FibRow>>> getFibs();

  /** Mapping: hostname -> vrfName -> fibTree */
  Map<String, Map<String, FibTree>> getFibTrees();

  Set<NodeInterfacePair> getFlowSinks();

  SortedMap<String, Map<Ip, SortedSet<Edge>>> getPolicyRouteFibNodeMap();

  SortedMap<String, SortedMap<String, IRib<AbstractRoute>>> getRibs();

  SortedSet<Edge> getTopologyEdges();
}
