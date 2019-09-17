create table users
(
    user_id bigint auto_increment,
    constraint users_pk
        primary key (user_id),
    user_name TEXT not null,
    password_hash TEXT not null
);

create table seen_episodes
(
    id bigint auto_increment,
    constraint seen_episodes_pk
        primary key (id),
    show_id bigint,
    season_id int,
    episode_id int,
    duration_min int,
    user_id bigint,
    constraint seen_episodes_fk foreign key (user_id)
    references users(user_id)
);