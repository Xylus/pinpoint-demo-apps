package com.navercorp.shopping.demo.order.mybatis;

import com.navercorp.shopping.demo.commons.domain.order.OrderStatus;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author HyunGil Jeong
 */
@MappedJdbcTypes(JdbcType.TINYINT)
@MappedTypes(OrderStatus.class)
public class OrderStatusTypeHandler implements TypeHandler<OrderStatus> {

    @Override
    public void setParameter(PreparedStatement preparedStatement, int i, OrderStatus orderStatus, JdbcType jdbcType) throws SQLException {
        preparedStatement.setInt(i, orderStatus.getCode());
    }

    @Override
    public OrderStatus getResult(ResultSet resultSet, String s) throws SQLException {
        return OrderStatus.findByCode(resultSet.getInt(s));
    }

    @Override
    public OrderStatus getResult(ResultSet resultSet, int i) throws SQLException {
        return OrderStatus.findByCode(resultSet.getInt(i));
    }

    @Override
    public OrderStatus getResult(CallableStatement callableStatement, int i) throws SQLException {
        return OrderStatus.findByCode(callableStatement.getInt(i));
    }
}
