<script setup lang="ts">
import { RouterLink, RouterView } from "vue-router";
import { onMounted } from "vue";

onMounted(() => {
  console.log("App");
});
</script>

<template>
  <div>
    <v-container fluid>
      <v-row>
        <v-col cols="2" md="2" sm="3">
          <header>
            <nav class="fixed-nav-bar">
              <v-card color="var(--tertiary-color)" height="100vh" width="100%">
                <RouterLink to="/"
                  ><v-btn color="var(--primary-color)" class="my-1" width="100%"
                    >Início</v-btn
                  ></RouterLink
                >
                <RouterLink to="/files">
                  <v-btn color="var(--primary-color)" class="my-1" width="100%"
                    >Arquivos</v-btn
                  >
                </RouterLink>
                <RouterLink to="/about"
                  ><v-btn color="var(--primary-color)" class="my-2" width="100%"
                    >Sobre</v-btn
                  ></RouterLink
                >
              </v-card>
            </nav>
          </header>
        </v-col>
        <v-col cols="10" md="10" sm="9">
          <RouterView />
        </v-col>
      </v-row>
    </v-container>
  </div>
</template>

<style scoped>
.fixed-nav-bar {
  position: fixed;
  width: 15%;
  /*z-index: auto;*/
}
</style>
