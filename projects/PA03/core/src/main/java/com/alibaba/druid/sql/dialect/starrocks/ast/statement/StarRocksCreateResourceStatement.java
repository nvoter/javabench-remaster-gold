package com.alibaba.druid.sql.dialect.starrocks.ast.statement;

import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.SQLStatementImpl;
import com.alibaba.druid.sql.ast.statement.SQLAssignItem;
import com.alibaba.druid.sql.ast.statement.SQLCreateStatement;
import com.alibaba.druid.sql.ast.statement.SQLDDLStatement;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import java.util.LinkedList;
import java.util.List;

public class StarRocksCreateResourceStatement extends SQLStatementImpl implements SQLDDLStatement, SQLCreateStatement {

    private SQLName name;

    private List<SQLAssignItem> properties = new LinkedList<>();

    private boolean external;

    public StarRocksCreateResourceStatement() {
        // TODO
    }

    public StarRocksCreateResourceStatement(DbType dbType) {
        super(dbType);
        // TODO
    }

    public SQLName getName() {
        return name;
    }

    public void setName(SQLName x) {
        // TODO
    }

    public List<SQLAssignItem> getProperties() {
        return properties;
    }

    public void setProperties(List<SQLAssignItem> properties) {
        this.properties = properties;
    }

    public void addProperty(SQLAssignItem assignItem) {
        // TODO
    }

    public void addProperty(SQLExpr key, SQLExpr value) {
        // TODO
    }

    public void addProperty(String key, String value) {
        // TODO
    }

    public boolean isExternal() {
        return external;
    }

    public void setExternal(boolean external) {
        // TODO
    }

    @Override
    protected void accept0(SQLASTVisitor visitor) {
        visitor.preVisit(this);
        visitor.visit(this);
        visitor.endVisit(this);
    }
}
