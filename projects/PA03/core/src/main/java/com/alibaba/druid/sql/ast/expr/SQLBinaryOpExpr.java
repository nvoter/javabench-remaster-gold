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
package com.alibaba.druid.sql.ast.expr;

import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.*;
import com.alibaba.druid.sql.visitor.ParameterizedVisitor;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.visitor.VisitorFeature;
import com.alibaba.druid.util.FnvHash;
import com.alibaba.druid.util.Utils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SQLBinaryOpExpr extends SQLExprImpl implements SQLReplaceable, Serializable, SQLDbTypedObject, Comparable<SQLBinaryOpExpr> {

    private static final long serialVersionUID = 1L;

    protected SQLExpr left;

    protected SQLExpr right;

    protected SQLBinaryOperator operator;

    protected DbType dbType;

    // only for parameterized output
    protected transient List<SQLObject> mergedList;

    public SQLBinaryOpExpr() {
        // TODO
    }

    public SQLBinaryOpExpr(DbType dbType) {
        // TODO
    }

    public SQLBinaryOpExpr(SQLExpr left, SQLBinaryOperator operator, SQLExpr right) {
        // TODO
    }

    public SQLBinaryOpExpr(SQLExpr left, SQLBinaryOperator operator, SQLExpr right, DbType dbType) {
        // TODO
    }

    public SQLBinaryOpExpr(SQLExpr left, SQLExpr right, SQLBinaryOperator operator) {
        // TODO
    }

    public DbType getDbType() {
        return dbType;
    }

    public void setDbType(DbType dbType) {
        this.dbType = dbType;
    }

    public SQLExpr getLeft() {
        // TODO
        return null;
    }

    public void setLeft(SQLExpr left) {
        if (left != null) {
            left.setParent(this);
        }
        this.left = left;
    }

    public SQLExpr getRight() {
        // TODO
        return null;
    }

    public void setRight(SQLExpr right) {
        if (right != null) {
            right.setParent(this);
        }
        this.right = right;
    }

    public SQLBinaryOperator getOperator() {
        // TODO
        return null;
    }

    public void setOperator(SQLBinaryOperator operator) {
        this.operator = operator;
    }

    protected void accept0(SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            if (left != null) {
                left.accept(visitor);
            }
            if (right != null) {
                right.accept(visitor);
            }
        }
        visitor.endVisit(this);
    }

    @Override
    public List getChildren() {
        return Arrays.asList(this.left, this.right);
    }

    @Override
    public int hashCode() {
        // TODO
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        // TODO
        return false;
    }

    public boolean equals(SQLBinaryOpExpr other) {
        // TODO
        return false;
    }

    public boolean equalsIgoreOrder(SQLBinaryOpExpr other) {
        if (this == other) {
            return true;
        }
        if (other == null) {
            return false;
        }
        if (operator != other.operator) {
            return false;
        }
        return (Utils.equals(this.left, other.left) && Utils.equals(this.right, other.right)) || (Utils.equals(this.left, other.right) && Utils.equals(this.right, other.left));
    }

    protected void cloneTo(SQLBinaryOpExpr x) {
        super.cloneTo(x);
        if (left != null) {
            x.setLeft(left.clone());
        }
        if (right != null) {
            x.setRight(right.clone());
        }
        x.operator = operator;
        x.dbType = dbType;
        x.parenthesized = parenthesized;
        if (hint != null) {
            x.hint = hint.clone();
        }
        x.setParenthesized(parenthesized);
    }

    public SQLBinaryOpExpr clone() {
        SQLBinaryOpExpr x = new SQLBinaryOpExpr();
        cloneTo(x);
        return x;
    }

    public String toString() {
        // TODO
        return null;
    }

    public static SQLExpr combine(List<? extends SQLExpr> items, SQLBinaryOperator op) {
        if (items == null || op == null) {
            return null;
        }
        int size = items.size();
        if (size == 0) {
            return null;
        }
        if (size == 1) {
            return items.get(0);
        }
        SQLBinaryOpExpr expr = new SQLBinaryOpExpr(items.get(0), op, items.get(1));
        for (int i = 2; i < size; ++i) {
            SQLExpr item = items.get(i);
            expr = new SQLBinaryOpExpr(expr, op, item);
        }
        return expr;
    }

    public static List<SQLExpr> split(SQLBinaryOpExpr x) {
        // TODO
        return null;
    }

    public static List<SQLExpr> split(SQLExpr x, SQLBinaryOperator op) {
        // TODO
        return null;
    }

    public static List<SQLExpr> split(SQLBinaryOpExpr x, SQLBinaryOperator op) {
        // TODO
        return null;
    }

    public static void split(List<SQLExpr> outList, SQLExpr expr, SQLBinaryOperator op) {
        // TODO
    }

    public static SQLExpr and(SQLExpr a, SQLExpr b) {
        if (a == null) {
            return b;
        }
        if (b == null) {
            return a;
        }
        if (a instanceof SQLBinaryOpExprGroup) {
            SQLBinaryOpExprGroup group = (SQLBinaryOpExprGroup) a;
            if (group.getOperator() == SQLBinaryOperator.BooleanAnd) {
                group.add(b);
                return group;
            } else if (group.getOperator() == SQLBinaryOperator.BooleanOr && group.getItems().size() == 1) {
                a = group.getItems().get(0).clone();
            }
        }
        if (b instanceof SQLBinaryOpExpr) {
            SQLBinaryOpExpr bb = (SQLBinaryOpExpr) b;
            if (bb.operator == SQLBinaryOperator.BooleanAnd) {
                return and(and(a, bb.left), bb.right);
            }
        } else if (b instanceof SQLBinaryOpExprGroup) {
            SQLBinaryOpExprGroup group = (SQLBinaryOpExprGroup) b;
            if (group.getOperator() == SQLBinaryOperator.BooleanOr && group.getItems().size() == 1) {
                b = group.getItems().get(0).clone();
            }
        }
        if (a instanceof SQLBinaryOpExpr && b instanceof SQLBinaryOpExprGroup && ((SQLBinaryOpExprGroup) b).getOperator() == SQLBinaryOperator.BooleanAnd) {
            SQLBinaryOpExprGroup group = (SQLBinaryOpExprGroup) b;
            group.add(0, a);
            return group;
        }
        return new SQLBinaryOpExpr(a, SQLBinaryOperator.BooleanAnd, b);
    }

    public static SQLExpr and(SQLExpr a, SQLExpr b, SQLExpr c) {
        return and(and(a, b), c);
    }

    public static SQLExpr or(SQLExpr a, SQLExpr b) {
        if (a == null) {
            return b;
        }
        if (b == null) {
            return a;
        }
        if (a instanceof SQLBinaryOpExprGroup) {
            SQLBinaryOpExprGroup group = (SQLBinaryOpExprGroup) a;
            if (group.getOperator() == SQLBinaryOperator.BooleanOr) {
                group.add(b);
                return group;
            }
        }
        if (b instanceof SQLBinaryOpExpr) {
            SQLBinaryOpExpr bb = (SQLBinaryOpExpr) b;
            if (bb.operator == SQLBinaryOperator.BooleanOr) {
                return or(or(a, bb.left), bb.right);
            }
        }
        return new SQLBinaryOpExpr(a, SQLBinaryOperator.BooleanOr, b);
    }

    public static SQLExpr or(List<? extends SQLExpr> list) {
        if (list.isEmpty()) {
            return null;
        }
        SQLExpr first = list.get(0);
        for (int i = 1; i < list.size(); i++) {
            first = or(first, list.get(i));
        }
        return first;
    }

    public static SQLExpr andIfNotExists(SQLExpr a, SQLExpr b) {
        if (a == null) {
            return b;
        }
        if (b == null) {
            return a;
        }
        List<SQLExpr> groupListA = new ArrayList<SQLExpr>();
        List<SQLExpr> groupListB = new ArrayList<SQLExpr>();
        split(groupListA, a, SQLBinaryOperator.BooleanAnd);
        split(groupListB, b, SQLBinaryOperator.BooleanAnd);
        for (SQLExpr itemB : groupListB) {
            boolean exist = false;
            for (SQLExpr itemA : groupListA) {
                if (itemA.equals(itemB)) {
                    exist = true;
                } else if (itemA instanceof SQLBinaryOpExpr && itemB instanceof SQLBinaryOpExpr) {
                    if (((SQLBinaryOpExpr) itemA).equalsIgoreOrder((SQLBinaryOpExpr) itemB)) {
                        exist = true;
                    }
                }
            }
            if (!exist) {
                groupListA.add(itemB);
            }
        }
        return combine(groupListA, SQLBinaryOperator.BooleanAnd);
    }

    public static SQLBinaryOpExpr isNotNull(SQLExpr expr) {
        return new SQLBinaryOpExpr(expr, SQLBinaryOperator.IsNot, new SQLNullExpr());
    }

    public static SQLBinaryOpExpr isNull(SQLExpr expr) {
        return new SQLBinaryOpExpr(expr, SQLBinaryOperator.Is, new SQLNullExpr());
    }

    public boolean replace(SQLExpr expr, SQLExpr target) {
        SQLObject parent = getParent();
        if (left == expr) {
            if (target == null) {
                if (parent instanceof SQLReplaceable) {
                    return ((SQLReplaceable) parent).replace(this, right);
                } else {
                    return false;
                }
            }
            this.setLeft(target);
            return true;
        }
        if (right == expr) {
            if (target == null) {
                if (parent instanceof SQLReplaceable) {
                    return ((SQLReplaceable) parent).replace(this, left);
                } else {
                    return false;
                }
            }
            this.setRight(target);
            return true;
        }
        if (left instanceof SQLBinaryOpExpr) {
            final SQLBinaryOperator operator = ((SQLBinaryOpExpr) left).getOperator();
            if (operator == SQLBinaryOperator.BooleanAnd) {
                if (((SQLBinaryOpExpr) left).replace(expr, target)) {
                    return true;
                }
            }
        }
        return false;
    }

    public SQLExpr other(SQLExpr x) {
        if (x == left) {
            return right;
        }
        if (x == right) {
            return left;
        }
        return null;
    }

    public boolean contains(SQLExpr item) {
        if (item instanceof SQLBinaryOpExpr) {
            if (this.equalsIgoreOrder((SQLBinaryOpExpr) item)) {
                return true;
            }
            return left.equals(item) || right.equals(item);
        }
        return false;
    }

    public SQLDataType computeDataType() {
        if (operator == null) {
            return null;
        }
        if (operator.isRelational()) {
            return SQLBooleanExpr.DATA_TYPE;
        }
        SQLDataType leftDataType = null, rightDataType = null;
        if (left != null) {
            leftDataType = left.computeDataType();
        }
        if (right != null) {
            rightDataType = right.computeDataType();
        }
        switch(operator) {
            case Concat:
                if (leftDataType != null) {
                    return leftDataType;
                }
                if (rightDataType != null) {
                    return rightDataType;
                }
                return SQLCharExpr.DATA_TYPE;
            case BooleanXor:
            case Modulus:
            case Mod:
            case DIV:
            case Divide:
                if (leftDataType != null) {
                    return leftDataType;
                }
                if (rightDataType != null) {
                    return rightDataType;
                }
                return null;
            case Subtract:
            case Add:
            case Multiply:
                if (leftDataType != null) {
                    if (rightDataType != null) {
                        if (leftDataType.nameHashCode64() == FnvHash.Constants.BIGINT && rightDataType.nameHashCode64() == FnvHash.Constants.NUMBER) {
                            return rightDataType;
                        }
                        if (leftDataType.isInt() && rightDataType.nameHashCode64() == FnvHash.Constants.INTERVAL) {
                            return rightDataType;
                        }
                        if ((leftDataType.nameHashCode64() == FnvHash.Constants.DATE || leftDataType.nameHashCode64() == FnvHash.Constants.DATETIME || leftDataType.nameHashCode64() == FnvHash.Constants.TIMESTAMP) && (rightDataType.nameHashCode64() == FnvHash.Constants.DATE || rightDataType.nameHashCode64() == FnvHash.Constants.DATETIME || rightDataType.nameHashCode64() == FnvHash.Constants.TIMESTAMP)) {
                            return new SQLDataTypeImpl("BIGING");
                        }
                        if ((leftDataType.isString() && !rightDataType.isString()) || (!leftDataType.isString() && rightDataType.isString())) {
                            return null;
                        }
                    }
                    return leftDataType;
                }
                return null;
            default:
                return null;
        }
    }

    public boolean conditionContainsTable(String alias) {
        if (left == null || right == null) {
            return false;
        }
        if (left instanceof SQLPropertyExpr) {
            if (((SQLPropertyExpr) left).matchOwner(alias)) {
                return true;
            }
        } else if (left instanceof SQLBinaryOpExpr) {
            if (((SQLBinaryOpExpr) left).conditionContainsTable(alias)) {
                return true;
            }
        }
        if (right instanceof SQLPropertyExpr) {
            if (((SQLPropertyExpr) right).matchOwner(alias)) {
                return true;
            }
        } else if (right instanceof SQLBinaryOpExpr) {
            return ((SQLBinaryOpExpr) right).conditionContainsTable(alias);
        }
        return false;
    }

    public boolean conditionContainsColumn(String column) {
        if (left == null || right == null) {
            return false;
        }
        if (left instanceof SQLIdentifierExpr) {
            if (((SQLIdentifierExpr) left).nameEquals(column)) {
                return true;
            }
        } else if (right instanceof SQLIdentifierExpr) {
            if (((SQLIdentifierExpr) right).nameEquals(column)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Merges the given SQLBinaryOpExpr with its parent using a parameterized visitor.
     *
     * @param v the parameterized visitor used for merging
     * @param x the SQLBinaryOpExpr to be merged
     * @return the merged SQLBinaryOpExpr
     */
    public static SQLBinaryOpExpr merge(ParameterizedVisitor v, SQLBinaryOpExpr x) {
        SQLObject parent = x.parent;
        for (; ; ) {
            if (x.right instanceof SQLBinaryOpExpr) {
                SQLBinaryOpExpr rightBinary = (SQLBinaryOpExpr) x.right;
                if (x.left instanceof SQLBinaryOpExpr) {
                    SQLBinaryOpExpr leftBinaryExpr = (SQLBinaryOpExpr) x.left;
                    if (SQLExprUtils.equals(leftBinaryExpr.right, rightBinary)) {
                        x = leftBinaryExpr;
                        v.incrementReplaceCunt();
                        continue;
                    }
                }
                SQLExpr mergedRight = merge(v, rightBinary);
                if (mergedRight != x.right) {
                    x = new SQLBinaryOpExpr(x.left, x.operator, mergedRight);
                    v.incrementReplaceCunt();
                }
                x.setParent(parent);
            }
            break;
        }
        if (x.left instanceof SQLBinaryOpExpr) {
            SQLExpr mergedLeft = merge(v, (SQLBinaryOpExpr) x.left);
            if (mergedLeft != x.left) {
                SQLBinaryOpExpr tmp = new SQLBinaryOpExpr(mergedLeft, x.operator, x.right);
                tmp.setParent(parent);
                x = tmp;
                v.incrementReplaceCunt();
            }
        }
        // ID = ? OR ID = ? => ID = ?
        if (x.operator == SQLBinaryOperator.BooleanOr && !v.isEnabled(VisitorFeature.OutputParameterizedQuesUnMergeInList)) {
            if ((x.left instanceof SQLBinaryOpExpr) && (x.right instanceof SQLBinaryOpExpr)) {
                SQLBinaryOpExpr leftBinary = (SQLBinaryOpExpr) x.left;
                SQLBinaryOpExpr rightBinary = (SQLBinaryOpExpr) x.right;
                if (mergeEqual(leftBinary, rightBinary)) {
                    v.incrementReplaceCunt();
                    leftBinary.setParent(x.parent);
                    leftBinary.addMergedItem(rightBinary);
                    return leftBinary;
                }
                if (//
                SQLExprUtils.isLiteralExpr(leftBinary.left) && leftBinary.operator == SQLBinaryOperator.BooleanOr) {
                    if (mergeEqual(leftBinary.right, x.right)) {
                        v.incrementReplaceCunt();
                        leftBinary.addMergedItem(rightBinary);
                        return leftBinary;
                    }
                }
            }
        }
        return x;
    }

    /**
     * Adds the given SQLBinaryOpExpr to the merged list.
     *
     * @param item the SQLBinaryOpExpr to be added
     */
    private void addMergedItem(SQLBinaryOpExpr item) {
        if (mergedList == null) {
            mergedList = new ArrayList<SQLObject>();
        }
        mergedList.add(item);
    }

    /**
     * Retrieves the merged list of SQLObjects.
     *
     * @return the merged list of SQLObjects
     */
    public List<SQLObject> getMergedList() {
        return mergedList;
    }

    /**
     * Determines whether two SQL expressions can be merged based on equality.
     *
     * @param a the first SQL expression to compare
     * @param b the second SQL expression to compare
     * @return true if the two expressions are equal and can be merged; otherwise false
     */
    private static boolean mergeEqual(SQLExpr a, SQLExpr b) {
        if (!(a instanceof SQLBinaryOpExpr)) {
            return false;
        }
        if (!(b instanceof SQLBinaryOpExpr)) {
            return false;
        }
        SQLBinaryOpExpr binaryA = (SQLBinaryOpExpr) a;
        SQLBinaryOpExpr binaryB = (SQLBinaryOpExpr) b;
        if (binaryA.operator != SQLBinaryOperator.Equality) {
            return false;
        }
        if (binaryB.operator != SQLBinaryOperator.Equality) {
            return false;
        }
        if (!(binaryA.right instanceof SQLLiteralExpr || binaryA.right instanceof SQLVariantRefExpr)) {
            return false;
        }
        if (!(binaryB.right instanceof SQLLiteralExpr || binaryB.right instanceof SQLVariantRefExpr)) {
            return false;
        }
        return binaryA.left.equals(binaryB.left);
    }

    public static boolean isOr(SQLExpr x) {
        return x instanceof SQLBinaryOpExpr && ((SQLBinaryOpExpr) x).getOperator() == SQLBinaryOperator.BooleanOr;
    }

    public static boolean isAnd(SQLExpr x) {
        return x instanceof SQLBinaryOpExpr && ((SQLBinaryOpExpr) x).getOperator() == SQLBinaryOperator.BooleanAnd;
    }

    public boolean isLeftNameAndRightLiteral() {
        return left instanceof SQLName && right instanceof SQLLiteralExpr;
    }

    public boolean isLeftFunctionAndRightLiteral() {
        return left instanceof SQLMethodInvokeExpr && right instanceof SQLLiteralExpr;
    }

    public boolean isNameAndLiteral() {
        return (left instanceof SQLLiteralExpr && right instanceof SQLName) || (left instanceof SQLName && right instanceof SQLLiteralExpr);
    }

    public boolean isBothName() {
        return left instanceof SQLName && right instanceof SQLName;
    }

    @Override
    public int compareTo(SQLBinaryOpExpr o) {
        int leftResult = SQLExprComparor.compareTo(left, o.left);
        if (leftResult != 0) {
            return leftResult;
        }
        int opResult = this.operator.compareTo(o.operator);
        if (opResult != 0) {
            return opResult;
        }
        int rightResult = SQLExprComparor.compareTo(right, o.right);
        if (rightResult != 0) {
            return rightResult;
        }
        return 0;
    }

    public boolean isLeftLiteralAndRightName() {
        return right instanceof SQLName && left instanceof SQLLiteralExpr;
    }

    public static SQLBinaryOpExpr conditionEq(String column, String value) {
        return new SQLBinaryOpExpr(SQLUtils.toSQLExpr(column), SQLBinaryOperator.Equality, new SQLCharExpr(value));
    }

    public static SQLBinaryOpExpr conditionEq(String column, int value) {
        return new SQLBinaryOpExpr(SQLUtils.toSQLExpr(column), SQLBinaryOperator.Equality, new SQLIntegerExpr(value));
    }

    public static SQLBinaryOpExpr conditionLike(String column, String value) {
        return new SQLBinaryOpExpr(SQLUtils.toSQLExpr(column), SQLBinaryOperator.Like, new SQLCharExpr(value));
    }

    public static SQLBinaryOpExpr conditionLike(String column, SQLExpr value) {
        return new SQLBinaryOpExpr(SQLUtils.toSQLExpr(column), SQLBinaryOperator.Like, value);
    }

    public static SQLBinaryOpExpr eq(SQLExpr a, SQLExpr b) {
        return new SQLBinaryOpExpr(a, SQLBinaryOperator.Equality, b);
    }
}
