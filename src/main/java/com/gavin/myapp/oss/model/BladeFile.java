package com.gavin.myapp.oss.model;

import java.util.Objects;

/**
 * BladeFile
 *
 */
public class BladeFile {

    /**
     * 文件地址
     */
    private String link;
    /**
     * 域名地址
     */
    private String domain;
    /**
     * 文件名
     */
    private String name;
    /**
     * 初始文件名
     */
    private String originalName;
    /**
     * 附件表ID
     */
    private Long attachId;

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public Long getAttachId() {
        return attachId;
    }

    public void setAttachId(Long attachId) {
        this.attachId = attachId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BladeFile bladeFile = (BladeFile) o;
        return (
            Objects.equals(link, bladeFile.link) &&
            Objects.equals(domain, bladeFile.domain) &&
            Objects.equals(name, bladeFile.name) &&
            Objects.equals(originalName, bladeFile.originalName) &&
            Objects.equals(attachId, bladeFile.attachId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(link, domain, name, originalName, attachId);
    }

    @Override
    public String toString() {
        return (
            "BladeFile{" +
            "link='" +
            link +
            '\'' +
            ", domain='" +
            domain +
            '\'' +
            ", name='" +
            name +
            '\'' +
            ", originalName='" +
            originalName +
            '\'' +
            ", attachId=" +
            attachId +
            '}'
        );
    }
}
