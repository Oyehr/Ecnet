//
// 此文件是由 JavaTM Architecture for XML Binding (JAXB) 引用实现 v2.2.8-b130911.1802 生成的
// 请访问 <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// 在重新编译源模式时, 对此文件的所有修改都将丢失。
// 生成时间: 2018.04.20 时间 10:36:44 AM CST
//


package com.ecm.model.xsd_evidence;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlMixed;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>arrow complex type的 Java 类。
 *
 * <p>以下模式片段指定包含在此类中的预期内容。
 *
 * <pre>
 * &lt;complexType name="arrow">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="content" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="head">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="content" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="bodyContent" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                 &lt;/sequence>
 *                 &lt;attribute name="x" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *                 &lt;attribute name="y" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "arrow", propOrder = {
        "content"
})
public class Arrow {

    @XmlElementRefs({
            @XmlElementRef(name = "name", type = JAXBElement.class),
            @XmlElementRef(name = "content", type = JAXBElement.class),
            @XmlElementRef(name = "head", type = JAXBElement.class)
    })
    @XmlMixed
    protected List<Serializable> content;

    /**
     * Gets the value of the content property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the content property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getContent().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link Arrow.Head }{@code >}
     * {@link String }
     *
     *
     */
    public List<Serializable> getContent() {
        if (content == null) {
            content = new ArrayList<Serializable>();
        }
        return this.content;
    }


    /**
     * <p>anonymous complex type的 Java 类。
     *
     * <p>以下模式片段指定包含在此类中的预期内容。
     *
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="content" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="bodyContent" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *       &lt;/sequence>
     *       &lt;attribute name="x" type="{http://www.w3.org/2001/XMLSchema}integer" />
     *       &lt;attribute name="y" type="{http://www.w3.org/2001/XMLSchema}integer" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     *
     *
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
            "name",
            "content",
            "bodyContent"
    })
    public static class Head {

        @XmlElement(required = true, nillable = true)
        protected String name;
        @XmlElement(required = true, nillable = true)
        protected String content;
        @XmlElement(required = true, nillable = true)
        protected String bodyContent;
        @XmlAttribute(name = "x")
        protected BigInteger x;
        @XmlAttribute(name = "y")
        protected BigInteger y;

        /**
         * 获取name属性的值。
         *
         * @return
         *     possible object is
         *     {@link String }
         *
         */
        public String getName() {
            return name;
        }

        /**
         * 设置name属性的值。
         *
         * @param value
         *     allowed object is
         *     {@link String }
         *
         */
        public void setName(String value) {
            this.name = value;
        }

        /**
         * 获取content属性的值。
         *
         * @return
         *     possible object is
         *     {@link String }
         *
         */
        public String getContent() {
            return content;
        }

        /**
         * 设置content属性的值。
         *
         * @param value
         *     allowed object is
         *     {@link String }
         *
         */
        public void setContent(String value) {
            this.content = value;
        }

        /**
         * 获取bodyContent属性的值。
         *
         * @return
         *     possible object is
         *     {@link String }
         *
         */
        public String getBodyContent() {
            return bodyContent;
        }

        /**
         * 设置bodyContent属性的值。
         *
         * @param value
         *     allowed object is
         *     {@link String }
         *
         */
        public void setBodyContent(String value) {
            this.bodyContent = value;
        }

        /**
         * 获取x属性的值。
         *
         * @return
         *     possible object is
         *     {@link BigInteger }
         *
         */
        public BigInteger getX() {
            return x;
        }

        /**
         * 设置x属性的值。
         *
         * @param value
         *     allowed object is
         *     {@link BigInteger }
         *
         */
        public void setX(BigInteger value) {
            this.x = value;
        }

        /**
         * 获取y属性的值。
         *
         * @return
         *     possible object is
         *     {@link BigInteger }
         *
         */
        public BigInteger getY() {
            return y;
        }

        /**
         * 设置y属性的值。
         *
         * @param value
         *     allowed object is
         *     {@link BigInteger }
         *
         */
        public void setY(BigInteger value) {
            this.y = value;
        }

    }

}
