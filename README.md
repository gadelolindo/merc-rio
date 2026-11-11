# Mercúrio (Minecraft Java 26.3 + Fabric)

Aba "Mercúrio" no criativo com 4 itens inspirados em mobs.

## Como compilar
**Jeito mais seguro:** gere um projeto novo em https://fabricmc.net/develop/template/
(Minecraft 26.3, nome do mod "mercurio", package `com.mercurio`), depois copie a pasta `src/`
deste zip por cima da do template e rode `./gradlew build`.

**Ou** use este projeto direto (precisa do Gradle e do JDK 25 instalados): `gradle wrapper` e depois `./gradlew build`.
O jar sai em `build/libs/` (use o que NÃO termina em `-sources`).
Instale na pasta `mods` junto com o Fabric API.

Para testar no jogo: `./gradlew runClient`.
