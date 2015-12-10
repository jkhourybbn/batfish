package org.batfish.question.static_route_expr;

import org.batfish.question.Environment;
import org.batfish.representation.StaticRoute;

public final class VarStaticRouteExpr extends BaseStaticRouteExpr {

   private final String _var;

   public VarStaticRouteExpr(String var) {
      _var = var;
   }

   @Override
   public StaticRoute evaluate(Environment environment) {
      return environment.getStaticRoutes().get(_var);
   }

}
