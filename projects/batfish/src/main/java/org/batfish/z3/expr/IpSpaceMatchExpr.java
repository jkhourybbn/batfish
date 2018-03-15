package org.batfish.z3.expr;

import java.util.Objects;
import org.batfish.datamodel.IpSpace;
import org.batfish.z3.expr.visitors.ExprVisitor;
import org.batfish.z3.expr.visitors.GenericBooleanExprVisitor;
import org.batfish.z3.expr.visitors.IpSpaceBooleanExprTransformer;

public class IpSpaceMatchExpr extends BooleanExpr {

  private final BooleanExpr _expr;

  public IpSpaceMatchExpr(IpSpace ipSpace, boolean useSrc, boolean useDst) {
    _expr = ipSpace.accept(new IpSpaceBooleanExprTransformer(useSrc, useDst));
  }

  @Override
  public void accept(ExprVisitor visitor) {
    visitor.visitMatchIpSpaceExpr(this);
  }

  @Override
  public <R> R accept(GenericBooleanExprVisitor<R> visitor) {
    return visitor.visitMatchIpSpaceExpr(this);
  }

  @Override
  public boolean exprEquals(Expr e) {
    return Objects.equals(_expr, ((IpSpaceMatchExpr) e)._expr);
  }

  public BooleanExpr getExpr() {
    return _expr;
  }

  @Override
  public int hashCode() {
    return Objects.hash(_expr);
  }
}
