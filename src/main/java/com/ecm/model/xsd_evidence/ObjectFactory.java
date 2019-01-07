//
// 此文件是由 JavaTM Architecture for XML Binding (JAXB) 引用实现 v2.2.8-b130911.1802 生成的
// 请访问 <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// 在重新编译源模式时, 对此文件的所有修改都将丢失。
// 生成时间: 2018.04.20 时间 10:36:44 AM CST
//


package com.ecm.model.xsd_evidence;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the com.ecm.model.xsd_evidence package.
 * <p>An ObjectFactory allows you to programatically
 * construct new instances of the Java representation
 * for XML content. The Java representation of XML
 * content can consist of schema derived interfaces
 * and classes representing the binding of schema
 * type definitions, element declarations and model
 * groups.  Factory methods for each of these are
 * provided in this class.
 *
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _EvidenceHeadsHead_QNAME = new QName("", "head");
    private final static QName _JointName_QNAME = new QName("", "name");
    private final static QName _JointContent_QNAME = new QName("", "content");
    private final static QName _EcmRelationsRelation_QNAME = new QName("", "relation");
    private final static QName _EcmRelationsRelationJointFactContent_QNAME = new QName("", "factContent");
    private final static QName _EcmEvidencesEvidence_QNAME = new QName("", "evidence");
    private final static QName _EcmRelationsRelationJoint_QNAME = new QName("", "joint");
    private final static QName _EcmRelationsRelationArrows_QNAME = new QName("", "arrows");
    private final static QName _EvidenceTrust_QNAME = new QName("", "trust");
    private final static QName _EvidenceReason_QNAME = new QName("", "reason");
    private final static QName _EvidenceCommitter_QNAME = new QName("", "committer");
    private final static QName _EvidenceHeads_QNAME = new QName("", "heads");
    private final static QName _EvidenceType_QNAME = new QName("", "type");
    private final static QName _EcmEvidences_QNAME = new QName("", "evidences");
    private final static QName _EcmRelations_QNAME = new QName("", "relations");
    private final static QName _EcmFacts_QNAME = new QName("", "facts");
    private final static QName _EcmRelationsRelationArrowsArrow_QNAME = new QName("", "arrow");
    private final static QName _FactJoints_QNAME = new QName("", "joints");
    private final static QName _EcmFactsFact_QNAME = new QName("", "fact");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.ecm.model.xsd_evidence
     *
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Ecm }
     *
     */
    public Ecm createEcm() {
        return new Ecm();
    }

    /**
     * Create an instance of {@link Arrow }
     *
     */
    public Arrow createArrow() {
        return new Arrow();
    }

    /**
     * Create an instance of {@link Evidence }
     *
     */
    public Evidence createEvidence() {
        return new Evidence();
    }

    /**
     * Create an instance of {@link Fact }
     *
     */
    public Fact createFact() {
        return new Fact();
    }

    /**
     * Create an instance of {@link Ecm.Relations }
     *
     */
    public Ecm.Relations createEcmRelations() {
        return new Ecm.Relations();
    }

    /**
     * Create an instance of {@link Ecm.Relations.Relation }
     *
     */
    public Ecm.Relations.Relation createEcmRelationsRelation() {
        return new Ecm.Relations.Relation();
    }

    /**
     * Create an instance of {@link Ecm.Evidences }
     *
     */
    public Ecm.Evidences createEcmEvidences() {
        return new Ecm.Evidences();
    }

    /**
     * Create an instance of {@link Ecm.Facts }
     *
     */
    public Ecm.Facts createEcmFacts() {
        return new Ecm.Facts();
    }

    /**
     * Create an instance of {@link com.ecm.model.xsd_evidence.Head }
     *
     */
    public com.ecm.model.xsd_evidence.Head createHead() {
        return new com.ecm.model.xsd_evidence.Head();
    }

    /**
     * Create an instance of {@link com.ecm.model.xsd_evidence.Joint }
     *
     */
    public com.ecm.model.xsd_evidence.Joint createJoint() {
        return new com.ecm.model.xsd_evidence.Joint();
    }

    /**
     * Create an instance of {@link Arrow.Head }
     *
     */
    public Arrow.Head createArrowHead() {
        return new Arrow.Head();
    }

    /**
     * Create an instance of {@link Evidence.Heads }
     *
     */
    public Evidence.Heads createEvidenceHeads() {
        return new Evidence.Heads();
    }

    /**
     * Create an instance of {@link Fact.Joints }
     *
     */
    public Fact.Joints createFactJoints() {
        return new Fact.Joints();
    }

    /**
     * Create an instance of {@link Ecm.Relations.Relation.Arrows }
     *
     */
    public Ecm.Relations.Relation.Arrows createEcmRelationsRelationArrows() {
        return new Ecm.Relations.Relation.Arrows();
    }

    /**
     * Create an instance of {@link Ecm.Relations.Relation.Joint }
     *
     */
    public Ecm.Relations.Relation.Joint createEcmRelationsRelationJoint() {
        return new Ecm.Relations.Relation.Joint();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link com.ecm.model.xsd_evidence.Head }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "", name = "head", scope = Evidence.Heads.class)
    public JAXBElement<com.ecm.model.xsd_evidence.Head> createEvidenceHeadsHead(com.ecm.model.xsd_evidence.Head value) {
        return new JAXBElement<com.ecm.model.xsd_evidence.Head>(_EvidenceHeadsHead_QNAME, com.ecm.model.xsd_evidence.Head.class, Evidence.Heads.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "", name = "name", scope = com.ecm.model.xsd_evidence.Joint.class)
    public JAXBElement<String> createJointName(String value) {
        return new JAXBElement<String>(_JointName_QNAME, String.class, com.ecm.model.xsd_evidence.Joint.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "", name = "content", scope = com.ecm.model.xsd_evidence.Joint.class)
    public JAXBElement<String> createJointContent(String value) {
        return new JAXBElement<String>(_JointContent_QNAME, String.class, com.ecm.model.xsd_evidence.Joint.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Ecm.Relations.Relation }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "", name = "relation", scope = Ecm.Relations.class)
    public JAXBElement<Ecm.Relations.Relation> createEcmRelationsRelation(Ecm.Relations.Relation value) {
        return new JAXBElement<Ecm.Relations.Relation>(_EcmRelationsRelation_QNAME, Ecm.Relations.Relation.class, Ecm.Relations.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Arrow.Head }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "", name = "head", scope = Arrow.class)
    public JAXBElement<Arrow.Head> createArrowHead(Arrow.Head value) {
        return new JAXBElement<Arrow.Head>(_EvidenceHeadsHead_QNAME, Arrow.Head.class, Arrow.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "", name = "name", scope = Arrow.class)
    public JAXBElement<String> createArrowName(String value) {
        return new JAXBElement<String>(_JointName_QNAME, String.class, Arrow.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "", name = "content", scope = Arrow.class)
    public JAXBElement<String> createArrowContent(String value) {
        return new JAXBElement<String>(_JointContent_QNAME, String.class, Arrow.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "", name = "name", scope = Ecm.Relations.Relation.Joint.class)
    public JAXBElement<String> createEcmRelationsRelationJointName(String value) {
        return new JAXBElement<String>(_JointName_QNAME, String.class, Ecm.Relations.Relation.Joint.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "", name = "content", scope = Ecm.Relations.Relation.Joint.class)
    public JAXBElement<String> createEcmRelationsRelationJointContent(String value) {
        return new JAXBElement<String>(_JointContent_QNAME, String.class, Ecm.Relations.Relation.Joint.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "", name = "factContent", scope = Ecm.Relations.Relation.Joint.class)
    public JAXBElement<String> createEcmRelationsRelationJointFactContent(String value) {
        return new JAXBElement<String>(_EcmRelationsRelationJointFactContent_QNAME, String.class, Ecm.Relations.Relation.Joint.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Evidence }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "", name = "evidence", scope = Ecm.Evidences.class)
    public JAXBElement<Evidence> createEcmEvidencesEvidence(Evidence value) {
        return new JAXBElement<Evidence>(_EcmEvidencesEvidence_QNAME, Evidence.class, Ecm.Evidences.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Ecm.Relations.Relation.Joint }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "", name = "joint", scope = Ecm.Relations.Relation.class)
    public JAXBElement<Ecm.Relations.Relation.Joint> createEcmRelationsRelationJoint(Ecm.Relations.Relation.Joint value) {
        return new JAXBElement<Ecm.Relations.Relation.Joint>(_EcmRelationsRelationJoint_QNAME, Ecm.Relations.Relation.Joint.class, Ecm.Relations.Relation.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Ecm.Relations.Relation.Arrows }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "", name = "arrows", scope = Ecm.Relations.Relation.class)
    public JAXBElement<Ecm.Relations.Relation.Arrows> createEcmRelationsRelationArrows(Ecm.Relations.Relation.Arrows value) {
        return new JAXBElement<Ecm.Relations.Relation.Arrows>(_EcmRelationsRelationArrows_QNAME, Ecm.Relations.Relation.Arrows.class, Ecm.Relations.Relation.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "", name = "trust", scope = Evidence.class)
    public JAXBElement<String> createEvidenceTrust(String value) {
        return new JAXBElement<String>(_EvidenceTrust_QNAME, String.class, Evidence.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "", name = "reason", scope = Evidence.class)
    public JAXBElement<String> createEvidenceReason(String value) {
        return new JAXBElement<String>(_EvidenceReason_QNAME, String.class, Evidence.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "", name = "committer", scope = Evidence.class)
    public JAXBElement<String> createEvidenceCommitter(String value) {
        return new JAXBElement<String>(_EvidenceCommitter_QNAME, String.class, Evidence.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "", name = "name", scope = Evidence.class)
    public JAXBElement<String> createEvidenceName(String value) {
        return new JAXBElement<String>(_JointName_QNAME, String.class, Evidence.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Evidence.Heads }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "", name = "heads", scope = Evidence.class)
    public JAXBElement<Evidence.Heads> createEvidenceHeads(Evidence.Heads value) {
        return new JAXBElement<Evidence.Heads>(_EvidenceHeads_QNAME, Evidence.Heads.class, Evidence.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "", name = "type", scope = Evidence.class)
    public JAXBElement<String> createEvidenceType(String value) {
        return new JAXBElement<String>(_EvidenceType_QNAME, String.class, Evidence.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "", name = "content", scope = Evidence.class)
    public JAXBElement<String> createEvidenceContent(String value) {
        return new JAXBElement<String>(_JointContent_QNAME, String.class, Evidence.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Ecm.Evidences }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "", name = "evidences", scope = Ecm.class)
    public JAXBElement<Ecm.Evidences> createEcmEvidences(Ecm.Evidences value) {
        return new JAXBElement<Ecm.Evidences>(_EcmEvidences_QNAME, Ecm.Evidences.class, Ecm.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Ecm.Relations }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "", name = "relations", scope = Ecm.class)
    public JAXBElement<Ecm.Relations> createEcmRelations(Ecm.Relations value) {
        return new JAXBElement<Ecm.Relations>(_EcmRelations_QNAME, Ecm.Relations.class, Ecm.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Ecm.Facts }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "", name = "facts", scope = Ecm.class)
    public JAXBElement<Ecm.Facts> createEcmFacts(Ecm.Facts value) {
        return new JAXBElement<Ecm.Facts>(_EcmFacts_QNAME, Ecm.Facts.class, Ecm.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Arrow }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "", name = "arrow", scope = Ecm.Relations.Relation.Arrows.class)
    public JAXBElement<Arrow> createEcmRelationsRelationArrowsArrow(Arrow value) {
        return new JAXBElement<Arrow>(_EcmRelationsRelationArrowsArrow_QNAME, Arrow.class, Ecm.Relations.Relation.Arrows.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "", name = "name", scope = com.ecm.model.xsd_evidence.Head.class)
    public JAXBElement<String> createHeadName(String value) {
        return new JAXBElement<String>(_JointName_QNAME, String.class, com.ecm.model.xsd_evidence.Head.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "", name = "content", scope = com.ecm.model.xsd_evidence.Head.class)
    public JAXBElement<String> createHeadContent(String value) {
        return new JAXBElement<String>(_JointContent_QNAME, String.class, com.ecm.model.xsd_evidence.Head.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Fact.Joints }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "", name = "joints", scope = Fact.class)
    public JAXBElement<Fact.Joints> createFactJoints(Fact.Joints value) {
        return new JAXBElement<Fact.Joints>(_FactJoints_QNAME, Fact.Joints.class, Fact.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "", name = "name", scope = Fact.class)
    public JAXBElement<String> createFactName(String value) {
        return new JAXBElement<String>(_JointName_QNAME, String.class, Fact.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "", name = "content", scope = Fact.class)
    public JAXBElement<String> createFactContent(String value) {
        return new JAXBElement<String>(_JointContent_QNAME, String.class, Fact.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link com.ecm.model.xsd_evidence.Joint }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "", name = "joint", scope = Fact.Joints.class)
    public JAXBElement<com.ecm.model.xsd_evidence.Joint> createFactJointsJoint(com.ecm.model.xsd_evidence.Joint value) {
        return new JAXBElement<com.ecm.model.xsd_evidence.Joint>(_EcmRelationsRelationJoint_QNAME, com.ecm.model.xsd_evidence.Joint.class, Fact.Joints.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Fact }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "", name = "fact", scope = Ecm.Facts.class)
    public JAXBElement<Fact> createEcmFactsFact(Fact value) {
        return new JAXBElement<Fact>(_EcmFactsFact_QNAME, Fact.class, Ecm.Facts.class, value);
    }

}
