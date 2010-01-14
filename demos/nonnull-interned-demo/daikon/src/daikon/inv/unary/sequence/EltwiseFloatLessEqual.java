// ***** This file is automatically generated from EltwiseIntComparisons.java.jpp

package daikon.inv.unary.sequence;

import daikon.*;
import daikon.derive.*;
import daikon.derive.binary.*;
import daikon.inv.*;
import daikon.Quantify.QuantFlags;
// import daikon.inv.binary.twoScalar.CORECLASS;
import utilMDE.*;
import java.util.*;

  /**
   * Represents the invariant "<=" between adjacent elements
   * (x[i], x[i+1]) of a double sequence.  Prints as
   * <samp>x[] sorted by "<="</samp>.
   **/

public class EltwiseFloatLessEqual
  extends EltwiseFloatComparison
{
  // We are Serializable, so we specify a version to allow changes to
  // method signatures without breaking serialization.  If you add or
  // remove fields, you should change this number to the current date.
  static final long serialVersionUID = 20030822L;

  // Variables starting with dkconfig_ should only be set via the
  // daikon.config.Configuration interface.
  /**
   * Boolean.  True iff EltwiseIntComparison invariants should be considered.
   **/
  public static boolean dkconfig_enabled = true;

  static final boolean debugEltwiseIntComparison = false;

  protected EltwiseFloatLessEqual(PptSlice ppt) {
    super(ppt);
  }

  private static EltwiseFloatLessEqual proto;

  /** Returns the prototype invariant for EltwiseFloatLessEqual **/
  public static Invariant get_proto() {
    if (proto == null)
      proto = new EltwiseFloatLessEqual (null);
    return (proto);
  }

  /** returns whether or not this invariant is enabled **/
  public boolean enabled() {
    return dkconfig_enabled;
  }

  /** Non-equality EltwiseFloatLessEqual invariants are only valid on integral types **/
  public boolean instantiate_ok (VarInfo[] vis) {

    if (!valid_types (vis))
      return (false);

    return (true);
  }

  /** Instantiate the invariant on the specified slice **/
  protected Invariant instantiate_dyn (PptSlice slice) {
    return new EltwiseFloatLessEqual(slice);
  }

  public EltwiseFloatLessEqual clone() {
    EltwiseFloatLessEqual result = (EltwiseFloatLessEqual) super.clone();
    return result;
  }

  public String repr() {
    return "EltwiseFloatLessEqual" + varNames() + ": "
      + "falsified=" + falsified;
  }

  public String format_using(OutputFormat format) {
    if (format.isJavaFamily()) return format_java_family(format);

    if (format == OutputFormat.DAIKON) return format_daikon();
    if (format == OutputFormat.IOA) return format_ioa();
    if (format == OutputFormat.ESCJAVA) return format_esc();
    if (format == OutputFormat.SIMPLIFY) return format_simplify();

    return format_unimplemented(format);
  }

  public String format_daikon() {
    if (debugEltwiseIntComparison) {
      System.out.println(repr());
    }

    return (var().name() + " sorted by <=");
  }

  /* IOA */
  public String format_ioa() {
    Quantify.IOAQuantification quant = VarInfo.get_ioa_quantify (var(), var());

    String result = quant.getQuantifierExp() + "(" + quant.getMembershipRestriction(0) + " /\\ " + quant.getMembershipRestriction(1);

      // i \in X /\ j \in X /\ i+1 = j => X[i] = X[j]
      result +=
        " /\\ " + quant.getFreeVarName(0) + "+1 = " + quant.getFreeVarName(1);

    result += ") => " + quant.getVarIndexedString(0) + " = " + quant.getVarIndexedString(1);
    return result + quant.getClosingExp();
  }

  public String format_esc() {
    String[] form = VarInfo.esc_quantify (false, var(), var());

      return form[0] + "((i+1 == j) ==> (" + form[1] + " <= " + form[2] + "))" + form[3];
  }

  public String format_java_family(OutputFormat format) {
    return "daikon.Quant.eltwiseLTE(" + var().name_using(format) + ")";
  }

  public String format_simplify() {
    String[] form = VarInfo.simplify_quantify (QuantFlags.adjacent(),
                                               var(), var());

    String comparator = "<=";

    return form[0] + "(" + comparator + " " + form[1] + " " + form[2] + ")"
      + form[3];
  }

  public InvariantStatus check_modified(double[] a, int count) {
    for (int i=1; i<a.length; i++) {
      if (!(Global.fuzzy.lte (a[i-1], a[i]))) {
        return InvariantStatus.FALSIFIED;
      }
    }
    return InvariantStatus.NO_CHANGE;
  }

  public InvariantStatus add_modified(double[] a, int count) {
    return check_modified(a, count);
  }

  // Perhaps check whether all the arrays of interest have length 0 or 1.

  protected double computeConfidence() {

    return 1 - Math.pow(.5, ppt.num_samples());
  }

  public boolean isExact() {

    return false;
  }

  public boolean isSameFormula(Invariant other) {
    return (other instanceof EltwiseFloatLessEqual);
  }

  // Not pretty... is there another way?
  // Also, reasonably complicated, need to ensure exact correctness, not sure if the
  // regression tests test this functionality

  public boolean isExclusiveFormula(Invariant other) {
    // This whole approach is wrong in the case when the sequence can
    // ever consist of only one element.  For now, just forget
    // it. -SMcC
    if (true)
      return false;

    if (other instanceof EltwiseFloatComparison) {

      return (other instanceof EltwiseIntGreaterThan) || (other instanceof EltwiseFloatGreaterThan);
    }
    return false;
  }

  // Look up a previously instantiated invariant.
  public static EltwiseFloatLessEqual find(PptSlice ppt) {
    Assert.assertTrue(ppt.arity() == 1);
    for (Invariant inv : ppt.invs) {
      if (inv instanceof EltwiseFloatLessEqual)
        return (EltwiseFloatLessEqual) inv;
    }
    return null;
  }

  // Copied from IntComparison.
  // public boolean isExclusiveFormula(Invariant other)
  // {
  //   if (other instanceof IntComparison) {
  //     return core.isExclusiveFormula(((IntComparison) other).core);
  //   }
  //   if (other instanceof IntNonEqual) {
  //     return isExact();
  //   }
  //   return false;
  // }

  /**
   * This function returns whether a sample has been seen by this
   * Invariant that includes two or more entries in an array. For a 0
   * or 1 element array a, a[] sorted by any binary operation is
   * "vacuously true" because no check is ever made since the binary
   * operation requires two operands.  Thus although invariants of
   * this type are true regarding 0 or 1 length arrays, they are
   * meaningless.  This function is meant to be used in
   * isObviousImplied() to prevent such meaningless invariants from
   * being printed.
   */
  public boolean hasSeenNonTrivialSample() {
    ValueSet.ValueSetFloatArray vs = (ValueSet.ValueSetFloatArray) ppt.var_infos[0].get_value_set();
    return (vs.multi_arr_cnt() > 0);
  }

  public DiscardInfo isObviousDynamically(VarInfo[] vis) {
    DiscardInfo super_result = super.isObviousDynamically(vis);
    if (super_result != null) {
      return super_result;
    }

    if (!hasSeenNonTrivialSample()) {
      return new DiscardInfo(this, DiscardCode.obvious,
                             "No samples sequences of size >=2 were seen. Vacuously true.");
    }

    EltOneOfFloat eoo = EltOneOfFloat.find(ppt);
    if ((eoo != null) && eoo.enoughSamples() && (eoo.num_elts() == 1)) {
      return new DiscardInfo(this, DiscardCode.obvious, "The sequence contains all equal values.");
    }

    {
      // some relations imply others as follows: < -> <=, > -> >=
      // == -> <=, >=

      // This code lets through some implied invariants, here is how:
      // In the ESC, JML, Java modes of output, the invariants are guarded
      // before printing.  This guarding makes some of the invariants that
      // are searched for (example, NoDuplicates) unable to be found since it
      // won't find something of the form (a -> NoDuplicates).  This can
      // cause cases to exist, for example, the invariants (a -> b[] sorted
      // by !=), (a -> b[] has no duplicates) existing in the same ppt where
      // one is obviously implied by the other. I am not sure currently of
      // the best way with dealing with this, but I am going to allow these
      // other invariants to exist for now because they are not wrong, just
      // obvious.

      for (Invariant inv : ppt.invs) {

        if ((inv instanceof EltwiseIntLessThan) || (inv instanceof EltwiseFloatLessThan)) {
          String discardString = "The invariant holds for < which implies <=.";
          return new DiscardInfo(this, DiscardCode.obvious, discardString);
        } else if ((inv instanceof EltwiseIntEqual) || (inv instanceof EltwiseFloatEqual)) {
          String discardString = "The invariant holds for == which implies <=.";
          return new DiscardInfo(this, DiscardCode.obvious, discardString);
        }

      }

    }

    // sorted by (any operation) for an entire sequence -> sorted by that same
    // operation for a subsequence

    // also, sorted by < for entire -> sorted by <= for subsequence
    //       sorted by > for entire -> sorted by >= for subsequence

    Derivation deriv = vis[0].derived;

    if (deriv != null && ((deriv instanceof SequenceScalarSubsequence) || (deriv instanceof SequenceFloatSubsequence))) {
      // Find the slice with the full sequence, check for an invariant of this type
      PptSlice sliceToCheck;

      if (deriv instanceof SequenceScalarSubsequence)
        sliceToCheck = ppt.parent.findSlice(((SequenceScalarSubsequence)deriv).seqvar());
      else
        sliceToCheck = ppt.parent.findSlice(((SequenceFloatSubsequence)deriv).seqvar());

      if (sliceToCheck != null) {
        for (Invariant inv : sliceToCheck.invs) {

          if ((inv instanceof EltwiseIntLessThan) || (inv instanceof EltwiseFloatLessThan)) {
            String discardString = "This is a subsequence of a sequence for which the < invariant holds.";
            return new DiscardInfo(this, DiscardCode.obvious, discardString);
          }

          if (inv.getClass().equals(getClass())) {
            String discardString = "This is a subsequence of a sequence for which the same invariant holds.";
            return new DiscardInfo(this, DiscardCode.obvious, discardString);
          }
        }
      }
    }

    return null;
  }
}
