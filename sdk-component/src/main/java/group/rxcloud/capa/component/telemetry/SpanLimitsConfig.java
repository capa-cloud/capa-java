package group.rxcloud.capa.component.telemetry;

import java.io.Serializable;

/**
 * @author: chenyijiang
 * @date: 2021/11/3 17:24
 */
public class SpanLimitsConfig implements Serializable {

    private static final long serialVersionUID = 6918786580143929155L;

    private Integer maxNumAttributes;

    private Integer maxNumEvents;

    private Integer maxNumLinks;

    private Integer maxNumAttributesPerEvent;

    private Integer maxNumAttributesPerLink;

    private Integer maxAttributeValueLength;

    public Integer getMaxNumAttributes() {
        return maxNumAttributes;
    }

    public void setMaxNumAttributes(Integer maxNumAttributes) {
        this.maxNumAttributes = maxNumAttributes;
    }

    public Integer getMaxNumEvents() {
        return maxNumEvents;
    }

    public void setMaxNumEvents(Integer maxNumEvents) {
        this.maxNumEvents = maxNumEvents;
    }

    public Integer getMaxNumLinks() {
        return maxNumLinks;
    }

    public void setMaxNumLinks(Integer maxNumLinks) {
        this.maxNumLinks = maxNumLinks;
    }

    public Integer getMaxNumAttributesPerEvent() {
        return maxNumAttributesPerEvent;
    }

    public void setMaxNumAttributesPerEvent(Integer maxNumAttributesPerEvent) {
        this.maxNumAttributesPerEvent = maxNumAttributesPerEvent;
    }

    public Integer getMaxNumAttributesPerLink() {
        return maxNumAttributesPerLink;
    }

    public void setMaxNumAttributesPerLink(Integer maxNumAttributesPerLink) {
        this.maxNumAttributesPerLink = maxNumAttributesPerLink;
    }

    public Integer getMaxAttributeValueLength() {
        return maxAttributeValueLength;
    }

    public void setMaxAttributeValueLength(Integer maxAttributeValueLength) {
        this.maxAttributeValueLength = maxAttributeValueLength;
    }
}
