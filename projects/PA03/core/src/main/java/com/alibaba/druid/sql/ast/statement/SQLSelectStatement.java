/*
 * Copyright 1999-2017 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.SQLStatementImpl;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import java.util.Collections;
import java.util.List;

public class SQLSelectStatement extends SQLStatementImpl {

    protected SQLSelect select;

    public SQLSelectStatement() {
        // TODO
    }

    public SQLSelectStatement(DbType dbType) {
        super(dbType);
        // TODO
    }

    public SQLSelectStatement(SQLSelect select) {
        // TODO
    }

    public SQLSelectStatement(SQLSelect select, DbType dbType) {
        this(dbType);
        // TODO
    }

    public SQLSelect getSelect() {
        // TODO
        return null;
    }

    public void setSelect(SQLSelect select) {
        // TODO
    }

    public void output(StringBuilder buf) {
        this.select.output(buf);
    }

    protected void accept0(SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            if (this.select != null) {
                this.select.accept(visitor);
            }
        }
        visitor.endVisit(this);
    }

    public SQLSelectStatement clone() {
        // TODO
        return null;
    }

    @Override
    public List<SQLObject> getChildren() {
        return Collections.<SQLObject>singletonList(select);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SQLSelectStatement that = (SQLSelectStatement) o;
        return select != null ? select.equals(that.select) : that.select == null;
    }

    @Override
    public int hashCode() {
        return select != null ? select.hashCode() : 0;
    }

    public List<String> computeSelecteListAlias() {
        return select.computeSelecteListAlias();
    }

    public boolean addWhere(SQLExpr where) {
        // TODO
        return false;
    }

    public void addBeforeComment(String comment) {
        if (headHints == null && attributes == null && select.hints == null && select.getBeforeCommentsDirect() == null) {
            SQLSelectQueryBlock queryBlock = select.getQueryBlock();
            List<String> queryBlockBeforeComments = queryBlock.getBeforeCommentsDirect();
            if (queryBlockBeforeComments != null) {
                queryBlockBeforeComments.add(comment);
                return;
            }
        }
        super.addBeforeComment(comment);
    }
}
