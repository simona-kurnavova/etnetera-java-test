package com.etnetera.hr.data;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * Simple data entity describing basic properties of every JavaScript framework.
 * 
 * @author Etnetera
 *
 */
@Entity
public class JavaScriptFramework {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(nullable = false, length = 30)
	@NotEmpty
    @Size(max = 30)
	private String name;

	@Column
    private String [] version;

    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    private Date deprecationDate;

	@Column
	private Integer hypeLevel;

	public JavaScriptFramework() {
	}

	public JavaScriptFramework(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

    public Date getDeprecationDate() {
        return deprecationDate;
    }

    public void setDeprecationDate(Date deprecationDate) {
        this.deprecationDate = deprecationDate;
    }

	public Integer getHypeLevel() {
		return hypeLevel;
	}

	public void setHypeLevel(Integer hypeLevel) {
		this.hypeLevel = hypeLevel;
	}

    public String[] getVersion() {
        return version;
    }

    public void setVersion(String[] version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "JavaScriptFramework [id=" + id + ", name=" + name + "]";
    }
}
