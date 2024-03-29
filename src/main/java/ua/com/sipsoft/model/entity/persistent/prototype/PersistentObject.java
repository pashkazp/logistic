package ua.com.sipsoft.model.entity.persistent.prototype;

/**
 *
 * @author Pavlo Degtyaryev
 */
public interface PersistentObject {

    public String getId();

    public void setId(String id);

    public Integer getVersion();

    public void setVersion(Integer version);
}
