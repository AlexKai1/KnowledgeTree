package druidSqlParser;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.*;
import com.alibaba.druid.sql.ast.expr.*;
import com.alibaba.druid.sql.ast.statement.*;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitorAdapter;
import com.alibaba.druid.util.JdbcConstants;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DruidSqlParserCommonTest {

    /**
     * MySQL 解析器
     */
    public static class ExportTableAliasVisitor extends MySqlASTVisitorAdapter {
        private Map<String, SQLTableSource> aliasMap = new HashMap<String, SQLTableSource>();

        public boolean visit(SQLExprTableSource x) {
            String alias = x.getAlias();
            aliasMap.put(alias, x);
            return true;
        }

        public Map<String, SQLTableSource> getAliasMap() {
            return aliasMap;
        }
    }

    public void parserSql() {
        final String dbType = JdbcConstants.MYSQL; // JdbcConstants.MYSQL或者JdbcConstants.POSTGRESQL
        String sql = "select * from mytable a where a.id = 3";
        List<SQLStatement> stmtList = SQLUtils.parseStatements(sql, dbType);

        ExportTableAliasVisitor visitor = new ExportTableAliasVisitor();

        for (SQLStatement stmt : stmtList) {
            stmt.accept(visitor);
        }

        SQLTableSource tableSource = visitor.getAliasMap().get("a");
        System.out.println(tableSource);

    }

    public void fullEnhanceSql(String sql) {
        // 解析
        List<SQLStatement> statements = SQLUtils.parseStatements(sql, JdbcConstants.MYSQL);
        // 只考虑一条语句
        SQLStatement statement = statements.get(0);
        // 只考虑查询语句
        SQLSelectStatement sqlSelectStatement = (SQLSelectStatement) statement;
        SQLSelectQuery sqlSelectQuery = sqlSelectStatement.getSelect().getQuery();
        // 非union的查询语句
        if (sqlSelectQuery instanceof SQLSelectQueryBlock) {
            SQLSelectQueryBlock sqlSelectQueryBlock = (SQLSelectQueryBlock) sqlSelectQuery;
            // 获取字段列表
            List<SQLSelectItem> selectItems = sqlSelectQueryBlock.getSelectList();
            selectItems.forEach(x -> {
                // 处理---------------------
                System.out.println(x);

            });
            // 获取表
            SQLTableSource table = sqlSelectQueryBlock.getFrom();
            System.out.println(table.toString());
            // 普通单表
            if (table instanceof SQLExprTableSource) {
                // 处理---------------------
                System.out.println("普通单表");
                // join多表
            } else if (table instanceof SQLJoinTableSource) {
                // 处理---------------------
                System.out.println("join多表");
                // 子查询作为表
            } else if (table instanceof SQLSubqueryTableSource) {
                // 处理---------------------
                System.out.println("子查询作为表");
            }
            // 获取where条件
            SQLExpr where = sqlSelectQueryBlock.getWhere();
            // 如果是二元表达式
            if (where instanceof SQLBinaryOpExpr) {
                SQLBinaryOpExpr sqlBinaryOpExpr = (SQLBinaryOpExpr) where;
                SQLExpr left = sqlBinaryOpExpr.getLeft();
                SQLBinaryOperator operator = sqlBinaryOpExpr.getOperator();
                SQLExpr right = sqlBinaryOpExpr.getRight();


                SQLBinaryOpExpr sqlObject = (SQLBinaryOpExpr) left.getChildren().get(0);
                System.out.println(sqlObject.getLeft());
                System.out.println(sqlObject.getRight());
                System.out.println("二元表达");
                System.out.println(left);
                System.out.println(right);
                System.out.println(operator);

                // 如果是子查询
            } else if (where instanceof SQLInSubQueryExpr) {
                SQLInSubQueryExpr sqlInSubQueryExpr = (SQLInSubQueryExpr) where;
                System.out.println("子查询");
                // 处理---------------------
            }
            // 获取分组
            SQLSelectGroupByClause groupBy = sqlSelectQueryBlock.getGroupBy();
            System.out.println("groupBy :" + groupBy);
            // 处理---------------------
            // 获取排序
            SQLOrderBy orderBy = sqlSelectQueryBlock.getOrderBy();
            System.out.println("orderBy :" + orderBy);
            // 处理---------------------
            // 获取分页
            SQLLimit limit = sqlSelectQueryBlock.getLimit();
            System.out.println("limit :" + limit);
            // 处理---------------------

            // union的查询语句
        } else if (sqlSelectQuery instanceof SQLUnionQuery) {
            System.out.println("union的查询语句");
            // 处理---------------------
        }
    }


    /**
     * 执行SQL解析模块
     */
    public void parseSqlModel(String sql) {
        SQLStatement sqlStatement = SQLUtils.parseSingleMysqlStatement(sql);
        SQLSelectStatement selectStatement = (SQLSelectStatement) sqlStatement;
        SQLSelectQuery sqlSelectQuery = selectStatement.getSelect().getQuery();
        // 非union的查询语句
        if (sqlSelectQuery instanceof SQLSelectQueryBlock) {
            SQLSelectQueryBlock sqlSelectQueryBlock = (SQLSelectQueryBlock) sqlSelectQuery;
            // 获取表
            SQLTableSource table = sqlSelectQueryBlock.getFrom();
            System.out.println(table.toString());
            // 要查询的字段，给接口的columns字段
            List<SQLSelectItem> selectList = sqlSelectQueryBlock.getSelectList();
            SQLExpr where = ((SQLSelectQueryBlock) sqlSelectQuery).getWhere();
            // 二元表达式
            if (where instanceof SQLBinaryOpExpr) {
                SQLBinaryOpExpr sqlBinaryOpExpr = (SQLBinaryOpExpr) where;
                SQLBinaryOperator operator = sqlBinaryOpExpr.getOperator();
                SQLBinaryOpExpr left = (SQLBinaryOpExpr) sqlBinaryOpExpr.getLeft();
                System.out.println(left.getOperator());
                SQLExpr right = sqlBinaryOpExpr.getRight();
                List children = sqlBinaryOpExpr.getChildren();
                System.out.println(right);
                System.out.println(children);
                System.out.println(children.size());
                for (Object c : children) {
                    System.out.println(c instanceof SQLInListExpr); // in 类型 =》 rowkwys
                    System.out.println(c instanceof SQLBinaryOpExpr); // and 类型 =》 条件

                    if (c instanceof SQLInListExpr) {
                        SQLInListExpr inlist = (SQLInListExpr) c;
                        List<SQLExpr> targetList = inlist.getTargetList();
                        System.out.println(targetList);
                    }
                    if (c instanceof SQLBinaryOpExpr) {
                        SQLBinaryOpExpr andObj = (SQLBinaryOpExpr) c;
                        List child = andObj.getChildren();
                        System.out.println(child);

                    }
                }
                System.out.println("test");
            }

        }
    }


    public SQLStatement getAst(String sql) {
        SQLStatement sqlStatement = SQLUtils.parseSingleMysqlStatement(sql);
        return sqlStatement;
    }

    public HashMap<String, String> parseSQL(String sql, HashMap<String, String> resultMap) {
        SQLStatement sqlStatement = getAst(sql);
        SQLSelectStatement selectStatement = (SQLSelectStatement) sqlStatement;
        SQLSelectQuery sqlSelectQuery = selectStatement.getSelect().getQuery();
        // 非union的查询语句,暂不支持复杂解析
        if (sqlSelectQuery instanceof SQLSelectQueryBlock) {
            SQLSelectQueryBlock sqlSelectQueryBlock = (SQLSelectQueryBlock) sqlSelectQuery;
            SQLTableSource table = sqlSelectQueryBlock.getFrom();
            resultMap.put("table", table.toString());
            SQLExpr where = sqlSelectQueryBlock.getWhere();
            // 判断语句中的where是否是二元表达式
            if (where instanceof SQLBinaryOpExpr) {
                SQLBinaryOpExpr sqlBinaryOpExpr = (SQLBinaryOpExpr) where;
                parseSubTree(sqlBinaryOpExpr, resultMap);
            }
        }
        return resultMap;
    }

    public void recursiveExec(SQLBinaryOpExpr sqlBinaryOpExpr, HashMap<String, String> resultMap) {
        List children = sqlBinaryOpExpr.getChildren();

        for (Object obj : children) {
            if (obj instanceof SQLInListExpr) {
                // where条件 是in的
                SQLInListExpr inObj = (SQLInListExpr) obj;
                // ['a,b,c,d']
                List<SQLExpr> targetList = inObj.getTargetList();
                String key = inObj.getExpr().toString();
                String value = StringUtils.join(targetList, ",");
                resultMap.put(key, value);
            } else if (obj instanceof SQLBinaryOpExpr) {
                // where 条件是and的
                SQLBinaryOpExpr andObj = (SQLBinaryOpExpr) obj;
                parseSubTree(andObj, resultMap);

            } else if (obj instanceof SQLIdentifierExpr) {
                SQLIdentifierExpr sqlIdentifierExpr = (SQLIdentifierExpr) obj;
                String name = sqlIdentifierExpr.getName();
                System.out.println(name);
            }
        }

    }

    public void parseSubTree(SQLBinaryOpExpr andObj, HashMap resultMap) {
        // 处理左子树
        if (andObj.getLeft() instanceof SQLBinaryOpExpr) {
            parseSubTree((SQLBinaryOpExpr) andObj.getLeft(), resultMap);
        } else if (andObj.getLeft() instanceof SQLInListExpr) {
            SQLInListExpr inObj = (SQLInListExpr) andObj.getLeft();
            // ['a,b,c,d']
            List<SQLExpr> targetList = inObj.getTargetList();
            String key = inObj.getExpr().toString();
            String value = StringUtils.join(targetList, ",");
            resultMap.put(key, value);
        } else {
            SQLIdentifierExpr childLeft = (SQLIdentifierExpr) andObj.getLeft();
            String key = childLeft.getName();
            String value = "";
            if (andObj.getRight() instanceof SQLIntegerExpr) {
                SQLIntegerExpr childRight = (SQLIntegerExpr) andObj.getRight();
                value = childRight.getNumber().toString();
            } else {
                SQLTextLiteralExpr childRight = (SQLTextLiteralExpr) andObj.getRight();
                value = childRight.getText();
            }
            resultMap.put(key, value);
        }
        // 处理右子树
        if (andObj.getRight() instanceof SQLBinaryOpExpr) {
            parseSubTree((SQLBinaryOpExpr) andObj.getRight(), resultMap);
        } else if (andObj.getRight() instanceof SQLInListExpr) {
            SQLInListExpr inObj = (SQLInListExpr) andObj.getRight();
            // ['a,b,c,d']
            List<SQLExpr> targetList = inObj.getTargetList();
            String key = inObj.getExpr().toString();
            String value = StringUtils.join(targetList, ",");
            resultMap.put(key, value);
        } else {
            SQLIdentifierExpr childLeft = (SQLIdentifierExpr) andObj.getLeft();
            String key = childLeft.getName();
            String value = "";
            if (andObj.getRight() instanceof SQLIntegerExpr) {
                SQLIntegerExpr childRight = (SQLIntegerExpr) andObj.getRight();
                value = childRight.getNumber().toString();
            } else {
                SQLTextLiteralExpr childRight = (SQLTextLiteralExpr) andObj.getRight();
                value = childRight.getText();
            }
            resultMap.put(key, value);
        }
    }


    public static void main(String[] args) {
        DruidSqlParserCommonTest testObj = new DruidSqlParserCommonTest();
        HashMap<String, String> map = new HashMap<String, String>();
//        String sql = " select name , id , rowkwy from HBaseTab where rowkey = 'abc' and name = 'alex' and id = 5 and h_list in ('a,b,c,d') ";
        String sql = " select name , id , rowkwy from HBaseTab where rowkey = 'abc'";
        HashMap<String, String> stringStringHashMap = testObj.parseSQL(sql, map);
        for (String key : stringStringHashMap.keySet()) {
            System.out.println("key = " + key);
            System.out.println("value = " + stringStringHashMap.get(key));
            System.out.println("=====line===");
        }
    }


}
