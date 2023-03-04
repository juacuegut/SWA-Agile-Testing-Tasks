package edu.upc.talent.swqa.campus;

public class UsersDb {
  public static final String usersTableDml =
        """
        create table users (
            id serial primary key,
            name text not null,
            surname text not null,
            email text not null,
            role text not null,
            group_id int not null,
            active boolean not null default true,
            foreign key (group_id) references groups(id)
        )
        """;

  public static final String groupsTableDml =
        """
        create table groups (
            id serial primary key,
            name text not null
        )
        """;
}
