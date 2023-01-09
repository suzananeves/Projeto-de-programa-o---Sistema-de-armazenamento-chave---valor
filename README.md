# Projeto de programação: Sistema de armazenamento chave-valor
Projeto desenvolvido para a disciplina de MCTA025-13 - Sistemas distribuídos na UFABC.
O projeto consiste em um sistema formado por 3 servidores, podendo ter múltiplos clientes com comunicação pelo protocolo TCP (Transmission Control Protocol).
O cliente tem um menu com 3 opções: INIT, PUT E GET. O início das atividades é pelo INIT, fornecendo o ip e a porta desses 3 servidores. Com o comando PUT o cliente envia uma chave e um valor para ser adicionado no servidor, o programa do cliente escolhe aleatoriamente um dos servidores, um desses servidores é o líder, apenas o líder pode guardar inicialmente as informações, assim, se o servidor escolhido pelo cliente for o líder, ele adiciona as informações em uma tabela hash e manda as informações para os outros servidores. Com o comando GET o cliente consegue extrair informações que já foram adicionadas.

# Observações:

A comunição TCP foi criada com base no vídeo disponibilizado na disciplina:
https://www.youtube.com/watch?v=nysfXweTI7o

