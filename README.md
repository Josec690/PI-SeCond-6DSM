# 🏠 SeCond

Aplicação de gerenciamento seguro e inteligente de condomínio desenvolvida em Kotlin Multiplatform com Jetpack Compose.
A mesma fará uma gestão de entrada e saída de pessoas de um condomínio, então o morador consegue através do próprio aplicativo, já comunicar quem vai chegar, quem vai sair, sem que precise interfonar com o porteiro.

## 👥 Equipe do Projeto

Este projeto foi realizado com a colaboração dos seguintes membros, listados em ordem alfabética:
- <a href="https://www.linkedin.com/in/rirfit/">Gabriel de Oliveira</a>
- <a href="https://www.linkedin.com/in/gustavo-morais-arruda/">Gustavo Morais</a>
- <a href="https://www.linkedin.com/in/joseclaudiley/">José Claudiley</a>
- <a href="https://www.linkedin.com/in/kauan-pires-21aa12288/">Kauan Pires</a>

## 📘 Descrição do Projeto

O **SeCond** é um frontend multiplataforma para gestão de condomínio, com telas de autenticação, dashboard e navegação entre os módulos do sistema.

O projeto utiliza tecnologias como:
- Kotlin Multiplatform
- Jetpack Compose
- Ktor Client
- Kotlin Serialization
- Navigation Compose
- Material Icons

Atualmente o sistema está organizado com foco em dois CRUDs principais:
- Avisos
- Convidados
- Encomendas
- Veículos

### Tema da interface
- O menu lateral possui um **Switch** para alternar entre tema claro e escuro.
- A preferencia de tema e **persistida no dispositivo** e mantida apos reiniciar o app.
- Comportamento atual: tema escuro como padrao inicial.

  
## 🚀 Instalação e Execução

### Pré-requisitos
- JDK compatível com o projeto
- Android Studio ou IntelliJ IDEA
- Gradle Wrapper disponível no projeto

### Como abrir o projeto
1. Baixe ou clone o repositório.
2. Abra a pasta raiz do projeto no Android Studio ou IntelliJ IDEA.
3. Aguarde o carregamento e a sincronização do Gradle.

### Execução no Android
1. Conecte um celular Android com a depuração USB ativada, ou use um emulador.
2. Selecione o módulo `composeApp`.
3. Clique em **Run** para executar o aplicativo.

### Execução no Desktop
No terminal, dentro da pasta raiz do projeto, execute:

```powershell
.\gradlew.bat :composeApp:run
```

### Validação da build
Se quiser apenas conferir se o projeto compila corretamente, execute:

```powershell
.\gradlew.bat build
```

## 📎 Observações

- O projeto foi estruturado como aplicação Kotlin Multiplatform com interface em Compose.
- A navegação entre telas foi organizada para separar autenticação, dashboard e CRUDs.
- Caso o backend remoto seja utilizado, verifique se a URL configurada em `RepositorioRemoto.kt` está correta.
- As imagens e logos do app devem ficar dentro dos recursos do projeto para serem carregadas corretamente.
