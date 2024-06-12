# cobranca-api
![bb drawio](https://github.com/vinicius-gomes/cobranca/assets/42249465/d3176737-be11-4841-b95d-cbf8e8f1ff78)

O projeto consiste numa topologia criada utilizando kafka-streams + spring boot + spring email.

A topologia consome dados de um evento que corresponde a um "débito" de um cliente, transforma tais dados e os repassa para novos tópicos e/ou serviços da maneira adequada de acordo com as necessidades de negócio.

OBS: o projeto contém um arquivo docker-compose.yml com configurações de zookeeper e broker kafka para maior agilidade e conveniência na execução do mesmo.

# comandos úteis do kafka:

CRIAR TÓPICOS:

./kafka-topics.sh --create --topic {TOPICO} --bootstrap-server {HOST:PORT}

DELETAR TÓPICOS:

./kafka-topics.sh --delete --topic {TOPICO} --bootstrap-server {HOST:PORT}

PRODUZIR EM UM TÓPICO ESPECIFICO:

./kafka-console-producer.sh --topic {TOPICO} --bootstrap-server {HOST:PORT}
