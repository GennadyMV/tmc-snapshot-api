insert into roles (id, rolename) values (1, 'user');
insert into users (id, username, password) values (2, 'username', '$2a$10$K5/TPyw2znk.N1KdJKnXL.wQaqgZPaCvBwVxV/vamVHuPGbc7WUzy'); -- username:password
insert into users_roles (roles_id, users_id) values (1, 2);
