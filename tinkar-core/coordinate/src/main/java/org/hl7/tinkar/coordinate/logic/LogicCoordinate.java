/* 
 * Licensed under the Apache License, Version 2.0 (the "License");
 *
 * You may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributions from 2013-2017 where performed either by US government 
 * employees, or under US Veterans Health Administration contracts. 
 *
 * US Veterans Health Administration contributions by government employees
 * are work of the U.S. Government and are not subject to copyright
 * protection in the United States. Portions contributed by government 
 * employees are USGovWork (17USC §105). Not subject to copyright. 
 * 
 * Contribution by contractors to the US Veterans Health Administration
 * during this period are contractually contributed under the
 * Apache License, Version 2.0.
 *
 * See: https://www.usa.gov/government-works
 * 
 * Contributions prior to 2013:
 *
 * Copyright (C) International Health Terminology Standards Development Organisation.
 * Licensed under the Apache License, Version 2.0.
 *
 */



package org.hl7.tinkar.coordinate.logic;

import org.hl7.tinkar.common.service.PrimitiveData;
import org.hl7.tinkar.coordinate.stamp.calculator.Latest;
import org.hl7.tinkar.coordinate.stamp.StampCoordinate;
import org.hl7.tinkar.entity.Entity;
import org.hl7.tinkar.entity.graph.DiTreeEntity;
import org.hl7.tinkar.terms.ConceptFacade;
import org.hl7.tinkar.terms.PatternFacade;
import org.hl7.tinkar.terms.PatternProxy;
import org.hl7.tinkar.terms.TinkarTerm;

import java.util.ArrayList;
import java.util.UUID;

/**
 * ImmutableCoordinate to manage the retrieval and display of logic information.
 *
 * Created by kec on 2/16/15.
 */
public interface LogicCoordinate {
    /**
     * 
     * @return a content based uuid, such that identical logic coordinates
     * will have identical uuids, and that different logic coordinates will 
     * always have different uuids.
     */
    default UUID getLogicCoordinateUuid() {
       ArrayList<UUID> uuidList = new ArrayList();
       Entity.provider().addSortedUuids(uuidList, classifierNid());
       Entity.provider().addSortedUuids(uuidList, descriptionLogicProfileNid());
       Entity.provider().addSortedUuids(uuidList, statedAxiomsPatternNid());
       Entity.provider().addSortedUuids(uuidList, inferredAxiomsPatternNid());
       Entity.provider().addSortedUuids(uuidList, conceptMemberPatternNid());
       Entity.provider().addSortedUuids(uuidList, statedNavigationPatternNid());
       Entity.provider().addSortedUuids(uuidList, inferredNavigationPatternNid());
       Entity.provider().addSortedUuids(uuidList, rootNid());
       return UUID.nameUUIDFromBytes(uuidList.toString().getBytes());
   }
   /**
    * Gets the classifier nid.
    *
    * @return concept nid for the classifier for this coordinate.
    */
   int classifierNid();

   default ConceptFacade classifier() {
      return Entity.getFast(classifierNid());
   }

   /**
    * Gets the description logic profile nid.
    *
    * @return concept nid for the description-logic profile for this coordinate.
    */
   int descriptionLogicProfileNid();

   default ConceptFacade descriptionLogicProfile() {
      return Entity.getFast(descriptionLogicProfileNid());
   }
   /**
    * Gets the inferred assemblage nid.
    *
    * @return concept nid for the assemblage where the inferred logical form
    * of concept definition graphs are stored.
    */
   int inferredAxiomsPatternNid();

   default PatternFacade inferredAxiomsPattern() {
      return PatternProxy.make(inferredAxiomsPatternNid());
   }
   /**
    * Gets the stated assemblage nid.
    *
    * @return concept nid for the assemblage where the stated logical form
    * of concept definition graphs are stored.
    */
   int statedAxiomsPatternNid();

   default PatternFacade statedAxiomsPattern() {
      return PatternProxy.make(statedAxiomsPatternNid());
   }

   /**
    * 
    * @return the nid for the assemblage within which the concepts to be classified are defined within. 
    */
   int conceptMemberPatternNid();
   default PatternFacade conceptMemberPattern() {
      return PatternProxy.make(conceptMemberPatternNid());
   }


   /**
    *
    * @return the nid of the concept identifying the digraph generated by stated axioms according to this logic coordinate.
    */
   default int statedNavigationPatternNid() {
      return TinkarTerm.STATED_NAVIGATION.nid();
   }
   default PatternFacade statedNavigationPattern() {
      return PatternFacade.make(statedNavigationPatternNid());
   }

   /**
    *
    * @return the concept identifying the digraph generated by classifying according to this logic coordinate.
    */
   default int inferredNavigationPatternNid() {
      return TinkarTerm.INFERRED_NAVIGATION.nid();
   }
   default PatternFacade inferredNavigationPattern() {
      return PatternFacade.make(inferredNavigationPatternNid());
   }

   default Latest<DiTreeEntity> getAxiomsVersion(int conceptNid, PremiseType premiseType, StampCoordinate stampCoordinate) {
      throw new UnsupportedOperationException();
//      int assemblageSequence;
//
//      if (premiseType == PremiseType.INFERRED) {
//         assemblageSequence = getInferredAssemblageNid();
//      } else {
//         assemblageSequence = getStatedAssemblageNid();
//      }
//      ImmutableList<LatestVersion<DiTreeEntity>> latestVersionList = Get.assemblageService()
//              .getSnapshot(LogicGraphVersion.class, stampCoordinateRecord)
//              .getLatestSemanticVersionsForComponentFromAssemblage(conceptNid, assemblageSequence);
//      if (latestVersionList.isEmpty()) {
//         return Optional.empty();
//      }
//      LatestVersion<DiTreeEntity> logicalDef = Get.assemblageService()
//              .getSnapshot(LogicGraphVersion.class, stampCoordinateRecord)
//              .getLatestSemanticVersionsForComponentFromAssemblage(conceptNid, assemblageSequence).get(0);
//
//      if (logicalDef.isPresent()) {
//         return Optional.of(logicalDef.get().getLogicalExpression());
//      }
//      return Optional.empty();
   }

   default Latest<DiTreeEntity> getStatedAxiomsVersion(int conceptNid, StampCoordinate stampCoordinate) {
      return getAxiomsVersion(conceptNid, PremiseType.STATED, stampCoordinate);
   }

   default Latest<DiTreeEntity> getInferredAxiomsVersion(ConceptFacade Concept, StampCoordinate stampCoordinate) {
      return getAxiomsVersion(Concept.nid(), PremiseType.INFERRED, stampCoordinate);
   }

   default Latest<DiTreeEntity> getStatedAxiomsVersion(ConceptFacade Concept, StampCoordinate stampCoordinate) {
      return getAxiomsVersion(Concept.nid(), PremiseType.STATED, stampCoordinate);
   }

   default Latest<DiTreeEntity> getInferredAxiomsVersion(int conceptNid, StampCoordinate stampCoordinate) {
      return getAxiomsVersion(conceptNid, PremiseType.INFERRED, stampCoordinate);
   }

   default String toUserString() {
      StringBuilder sb = new StringBuilder("   stated axiom pattern: ");
      sb.append(PrimitiveData.text(this.statedAxiomsPatternNid()));
      sb.append("\n   inferred axiom pattern: ");
      sb.append(PrimitiveData.text(this.inferredAxiomsPatternNid()));
      sb.append("\n   profile: ");
      sb.append(PrimitiveData.text(this.descriptionLogicProfileNid()));
      sb.append("\n   classifier: ");
      sb.append(PrimitiveData.text(this.classifierNid()));
      sb.append("\n   stated navigation pattern: ");
      sb.append(PrimitiveData.text(this.statedNavigationPatternNid()));
      sb.append("\n   inferred navigation pattern: ");
      sb.append(PrimitiveData.text(this.inferredNavigationPatternNid()));
      sb.append("\n   concept member pattern: ");
      sb.append(PrimitiveData.text(this.conceptMemberPatternNid()));
      sb.append("\n   root: ");
      sb.append(PrimitiveData.text(this.rootNid()));

      return sb.toString();
   }

   int rootNid();

   default ConceptFacade root() {
      return Entity.getFast(rootNid());
   }

   LogicCoordinateRecord toLogicCoordinateRecord();
}

