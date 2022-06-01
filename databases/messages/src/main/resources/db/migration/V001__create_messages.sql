create table messages
(
    id         serial primary key,
    body       varchar not null,
    created_at timestamp default current_timestamp,
    sent_at    timestamp
);
