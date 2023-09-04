package com.study.pojo;

public class UserConstants {
    public static final String MAPPING_TEMPLATE = "{\n" +
            "  \"mappings\": {\n" +
            "    \"properties\": {\n" +
            "      \"id\": {\n" +
            "        \"type\": \"keyword\",\n" +
            "        \"index\": true,\n" +
            "        \"copy_to\": \"all\"\n" +
            "      },\n" +
            "      \"name\": {\n" +
            "        \"type\": \"text\",\n" +
            "        \"analyzer\": \"ik_max_word\",\n" +
            "        \"index\": true,\n" +
            "        \"copy_to\": \"all\"\n" +
            "      },\n" +
            "      \"age\": {\n" +
            "        \"type\": \"integer\",\n" +
            "        \"index\": false\n" +
            "      },\n" +
            "      \"address\": {\n" +
            "        \"type\": \"geo_point\",\n" +
            "        \"index\": false\n" +
            "      },\n" +
            "      \"all\":{\n" +
            "        \"type\": \"text\",\n" +
            "        \"analyzer\": \"ik_smart\",\n" +
            "        \"index\": true\n" +
            "      }\n" +
            "    }\n" +
            "  }\n" +
            "}\n" +
            "}";
}
