package org.batfish.question;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.batfish.common.BatfishException;
import org.batfish.datamodel.Edge;
import org.batfish.datamodel.NeighborType;
import org.batfish.datamodel.Topology;
import org.batfish.datamodel.answers.Answer;
import org.batfish.datamodel.answers.NeighborsAnswerElement;
import org.batfish.datamodel.questions.NeighborsQuestion;
import org.batfish.main.Batfish;

public class NeighborsAnswer extends Answer {

   public NeighborsAnswer(Batfish batfish, NeighborsQuestion question) {
      Pattern dstNodeRegex;
      Pattern srcNodeRegex;

      try {
         dstNodeRegex = Pattern.compile(question.getDstNodeRegex());
         srcNodeRegex = Pattern.compile(question.getSrcNodeRegex());
      }
      catch (PatternSyntaxException e) {
         throw new BatfishException(
               String.format(
                     "One of the supplied regexes (%s  OR  %s) is not a valid java regex.",
                     question.getDstNodeRegex(), question.getSrcNodeRegex()), e);
      }

      NeighborsAnswerElement answerElement = new NeighborsAnswerElement();

      if (question.getNeighborTypes().isEmpty()
            || question.getNeighborTypes().contains(NeighborType.IP)) {
         Topology topology = batfish.computeTopology(
               batfish.loadConfigurations(), batfish.getEnvSettings());

         for (Edge edge : topology.getEdges()) {
            Matcher srcMatcher = srcNodeRegex.matcher(edge.getNode1());
            Matcher dstMatcher = dstNodeRegex.matcher(edge.getNode2());
            if (srcMatcher.matches() && dstMatcher.matches()) {
               answerElement.addIpEdge(edge);
            }
         }
      }

      for (NeighborType nType : question.getNeighborTypes()) {
         switch (nType) {
         case IP:
            break;
         case EBGP:
         case IBGP:
         default:
            throw new BatfishException("Unsupported NeighborType: "
                  + nType.toString());

         }
      }

      addAnswerElement(answerElement);
   }
}