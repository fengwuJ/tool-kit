package com.fengwuj.testdemo.typehandler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fengwuj.testdemo.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ListStringTypeHandler extends BaseTypeHandler<List<String>> {

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, List<String> list, JdbcType jdbcType) throws SQLException {
        if (CollectionUtils.isEmpty(list)){
            list = new ArrayList<>();
        }
        String data = JsonUtil.writeAsJsonString(list);
        preparedStatement.setString(i,data);
    }

    @Override
    public List<String> getNullableResult(ResultSet resultSet, String s) throws SQLException {
        String data = resultSet.getString(s);
        if (null == data || data.length() == 0){
            return new ArrayList<>();
        }

        return toList(resultSet.getString(s));
    }

    @Override
    public List<String> getNullableResult(ResultSet resultSet, int i) throws SQLException {
        String data = resultSet.getString(i);
        if (null == data || data.length() == 0){
            return new ArrayList<>();
        }

        return toList(resultSet.getString(i));
    }

    @Override
    public List<String> getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        String data = callableStatement.getString(i);
        if (null == data || data.length() == 0){
            return new ArrayList<>();
        }
        return toList(callableStatement.getString(i));
    }

    private List<String> toList(String str){
        TypeReference<List<String>> typeReference = new TypeReference<List<String>>() {
        };
        try {
            return JsonUtil.readJsonStringAsObject(str,typeReference);
        } catch (IOException e) {
            log.error("转换List数据失败",e);
        }
        return new ArrayList<>();
    }
}
