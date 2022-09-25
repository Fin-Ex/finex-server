# --- !Ups

create or replace function update_modify_date_trigger() returns trigger as $$
begin
    if row(NEW.*) is distinct from row(OLD.*) then
        NEW.modify_date = transaction_timestamp();;
        return NEW;;
    else
        return OLD;;
    end if;;
end;;
$$ language 'plpgsql';

create table if not exists users(
    id bigserial primary key,
    login varchar unique not null,
    password varchar not null,
    hash varchar not null,
    create_date timestamp default transaction_timestamp(),
    modify_date timestamp,
    auth_date timestamp,
    country varchar,
    ip_address varchar,
    email varchar,
    phone varchar,
    secret varchar
);
create trigger users_modify_date_trg before update on users for each row execute procedure update_modify_date_trigger();
comment on table users is 'Users';
comment on column users.login is 'User login name';
comment on column users.password is 'User password hash';
comment on column users.hash is 'User hash type';
comment on column users.create_date is 'Record create timestamp';
comment on column users.modify_date is 'Record modify timestamp';
comment on column users.auth_date is 'User last auth timestamp';
comment on column users.country is 'User country';
comment on column users.ip_address is 'User IP address';

create table if not exists user_restore_password_codes(
    id bigserial primary key,
    user_id bigint unique not null references users(id) on delete cascade on update cascade,
    code varchar not null,
    create_date timestamp default transaction_timestamp(),
    modify_date timestamp
);
create trigger user_restore_password_codes_modify_date_trg before update on user_restore_password_codes
    for each row execute procedure update_modify_date_trigger();
comment on table user_restore_password_codes is 'Users restore password codes';
comment on column user_restore_password_codes.id is 'ID';
comment on column user_restore_password_codes.user_id is 'Reference to specified user what requested password restore';
comment on column user_restore_password_codes.code is 'Secret code to approve change password operation';

create table if not exists user_totp_recovery_codes(
    id bigserial primary key,
    user_id bigint not null references users(id) on delete cascade on update cascade,
    code varchar(16) not null,
    create_date timestamp default transaction_timestamp(),
    modify_date timestamp
);
create trigger user_totp_recovery_codes_modify_date_trg before update on users
    for each row execute procedure update_modify_date_trigger();
create index if not exists user_totp_recovery_codes_user_id_idx on user_totp_recovery_codes(user_id);
create index if not exists user_totp_recovery_codes_user_id_code_idx on user_totp_recovery_codes(user_id, code);
comment on table user_totp_recovery_codes is 'Users recovery TOTP codes. Used if user doesnt have access to TOTP generator';
comment on column user_totp_recovery_codes.id is 'ID';
comment on column user_totp_recovery_codes.user_id is 'Reference to specified user';
comment on column user_totp_recovery_codes.code is 'Recovery code';

# --- !Downs

drop table if exists user_totp_recovery_codes;
drop table if exists user_restore_password_codes;
drop table if exists users;
drop function if exists update_modify_date_trigger() cascade;