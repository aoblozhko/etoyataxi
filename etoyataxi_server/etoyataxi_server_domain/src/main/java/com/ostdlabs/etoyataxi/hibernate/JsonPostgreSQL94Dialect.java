package com.ostdlabs.etoyataxi.hibernate;

import org.hibernate.dialect.PostgreSQL94Dialect;

import java.sql.Types;

public class JsonPostgreSQL94Dialect extends PostgreSQL94Dialect {

	public JsonPostgreSQL94Dialect() {
        this.registerColumnType(Types.JAVA_OBJECT, "jsonb");
	}
}