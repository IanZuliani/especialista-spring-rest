create table restaurante_usuario_responsavel(
restaurante_id BIGINT,
usuario_id BIGINT,

primary key (restaurante_id, usuario_id)
)engine=InnoDB default charset=utf8;

alter table restaurante_usuario_responsavel add constraint fk_restaurante_usuario_restaurante
foreign key (restaurante_id) references restaurante (id);

alter table restaurante_usuario_responsavel add constraint fk_restaurante_usuario
foreign key (usuario_id) references usuario (id);