package com.bobocode.json;

public class BacksonMapperDemo {
    public static void main(String[] args) {
        var json = """
                {
                  "first_name": "Andrii",
                  "last_name": "Shtramak",
                  "city": "Zarichchya"
                }
                """;

        var mapper = new BacksonMapper();
        final BacksonMapper.Person person = mapper.readObject(json, BacksonMapper.Person.class);
        System.out.println(person);
    }
}
