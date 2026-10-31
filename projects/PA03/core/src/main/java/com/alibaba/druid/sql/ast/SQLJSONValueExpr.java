package com.alibaba.druid.sql.ast;

import com.alibaba.druid.sql.visitor.SQLASTVisitor;

public class SQLJSONValueExpr extends SQLExprImpl {

    private SQLExpr json;

    private SQLExpr path;

    public SQLExpr getJson() {
        return json;
    }

    public void setJson(SQLExpr json) {
        // TODO
    }

    public SQLExpr getPath() {
        return path;
    }

    public void setPath(SQLExpr path) {
        // TODO
    }

    @Override
    public boolean equals(Object o) {
        // TODO
        return false;
    }

    @Override
    public int hashCode() {
        // TODO
        return 0;
    }

    @Override
    protected void accept0(SQLASTVisitor v) {
    }

    @Override
    public SQLExpr clone() {
        return null;
    }
}
