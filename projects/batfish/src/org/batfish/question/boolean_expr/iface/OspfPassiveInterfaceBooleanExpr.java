package org.batfish.question.boolean_expr.iface;

import org.batfish.question.Environment;
import org.batfish.question.interface_expr.InterfaceExpr;
import org.batfish.representation.Interface;

public final class OspfPassiveInterfaceBooleanExpr extends InterfaceBooleanExpr {

   public OspfPassiveInterfaceBooleanExpr(InterfaceExpr caller) {
      super(caller);
   }

   @Override
   public Boolean evaluate(Environment environment) {
      Interface iface = _caller.evaluate(environment);
      return iface.getOspfEnabled() && iface.getOspfPassive();
   }

}