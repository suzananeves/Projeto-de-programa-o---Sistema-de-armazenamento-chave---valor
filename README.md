# Projeto-de-programa-o---Sistema-de-armazenamento-chave---valor
Projeto desenvolvido para a disciplina de MCTA025-13 Sistemas distribuidos na UFABC.
O projeto consiste em um sistema formado por 3 servidores, podendo ter multiplos clientes.
Cliente:
cliente tem um menu com 3 opções: INIT, PUT E GET. O inicio das atividades é pelo init, fornecendo o ip e a porta desses 3 servidores. Com o Get o cliente envia uma chave
e um valor para ser adicionado no servidor, o programa do cliente escolhe aleatóriamente um dos servidores, um desses servidores é o lider, apenas o lider pode guardar 
inicialmente as informações, assim, se o servidor escolhido pelo cliente for o lider ele adiciona as informações em um tabela hash e manda as informações para os
outros servidores
