create table users(
                      id bigint auto_increment primary key,
                      first_name varchar(100) not null ,
                      last_name varchar(100) not null ,
                      email varchar(150) not null ,
                      password_hash varchar(255) not null ,
                      role varchar(20) not null ,
                      department varchar(100)  ,
                      manager_id bigint,
                      enabled boolean not null default true,
                      created_at datetime not null ,
                      updated_at datetime,
                      constraint uk_user_email unique (email),
                      constraint fk_user_manager foreign key (manager_id) references users(id)
);


create index idx_user_email on users(email);
create index idx_user_role on users(role);

create table policies(
                         id bigint auto_increment primary key,
                         name varchar(200) not null ,
                         applies_to_role varchar(20) not null ,
                         max_budget decimal(12,2) not null ,
                         max_travel_class varchar(50),
                         hard_block_on_violation boolean not null default false,
                         active boolean not null default true,
                         created_by_id bigint,
                         created_at datetime not null ,
                         updated_at datetime,
                         constraint fk_policy_creator foreign key (created_by_id) references users(id)
);

create index idx_policy_role on policies(applies_to_role);
create index idx_policy_active on policies(active);

create table travel_requests(
                                id bigint auto_increment primary key,
                                employee_id bigint not null,
                                destination varchar(200) not null,
                                start_date date not null,
                                end_date date not null,
                                purpose varchar(500) not null,
                                estimated_cost decimal(12,2) not null,
                                status varchar(30) not null default 'DRAFT',
                                travel_class varchar(50),
                                manager_approver_id bigint,
                                manager_action_at datetime,
                                manager_comments varchar(1000),
                                finance_approver_id bigint,
                                finance_action_at datetime,
                                finance_comments varchar(1000),
                                created_at datetime not null,
                                updated_at datetime,
                                submitted_at datetime,
                                completed_at datetime,
                                constraint fk_tr_employee foreign key (employee_id) references users(id),
                                constraint fk_tr_manager foreign key (manager_approver_id) references users(id),
                                constraint fk_tr_finance foreign key (finance_approver_id) references users(id)
);

create index idx_tr_status on travel_requests(status);
create index idx_tr_employee on travel_requests(employee_id);
create index idx_tr_dates on travel_requests(start_date, end_date);

create table itineraries(
                            id bigint auto_increment primary key,
                            travel_request_id bigint not null ,
                            segment_type varchar(20) not null ,
                            from_location varchar(200) not null ,
                            to_location varchar(200) not null ,
                            start_time datetime not null,
                            end_time datetime not null,
                            details varchar(500),
                            constraint fk_itin_request foreign key (travel_request_id) references travel_requests(id) on delete cascade
);

create index idx_itin_request on itineraries(travel_request_id);

create table expenses(
                         id bigint auto_increment primary key,
                         travel_request_id bigint not null ,
                         category varchar(30) not null ,
                         claimed_amount decimal(12,2) not null ,
                         approved_amount decimal(12,2),
                         description varchar(500) not null ,
                         expense_date date not null ,
                         receipt_path varchar(500),
                         status varchar(30) not null default 'PENDING',
                         approved_by_id bigint,
                         approved_at datetime,
                         finance_comments varchar(1000),
                         created_at datetime not null,
                         updated_at datetime,
                         constraint fk_exp_request foreign key (travel_request_id) references travel_requests(id) on delete cascade ,
                         constraint fk_exp_approver foreign key (approved_by_id) references users(id)
);

create index idx_exp_request on expenses(travel_request_id);
create index idx_exp_status on expenses(status);

create table reimbursements
(
    id bigint primary key auto_increment,
    expense_id bigint not null ,
    amount decimal(12,2) not null ,
    status varchar(30) not null default 'PENDING',
    processed_by_id bigint,
    processed_at datetime,
    reference_number varchar(100),
    created_at datetime not null ,
    updated_at datetime ,
    constraint uk_reimb_expense unique (expense_id),
    constraint fk_reimb_expense foreign key (expense_id) references expenses(id) on delete cascade ,
    constraint fk_reimb_processor foreign key (processed_by_id) references users(id)
);

create index idx_reimb_status on reimbursements(status);

create table audit_logs
(
    id bigint auto_increment primary key,
    timestamp datetime not null ,
    actor_id bigint not null ,
    actor_name varchar(200) not null ,
    actor_role varchar(20) not null ,
    action varchar(20) not null ,
    entity_type varchar(20) not null ,
    entity_id bigint not null ,
    comments varchar(1000)
);

create index idx_audit_timestamp on audit_logs(timestamp);
create index idx_audit_actor on audit_logs(actor_id);
create index idx_audit_entity on audit_logs(entity_type, entity_id);

create table notifications
(
    id bigint auto_increment primary key,
    recipient_id bigint not null ,
    title varchar(200) not null ,
    message varchar(1000) not null ,
    link_url varchar(500),
    read_flag boolean not null default false,
    created_at datetime not null ,
    read_at datetime ,
    constraint fk_notif_recipient foreign key (recipient_id) references users(id) on delete cascade
);

create index idx_notif_recipient on notifications(recipient_id, read_flag);

