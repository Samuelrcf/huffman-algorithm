<template>
  <div>
    <FileInput :title="compressFiles" :buttonText="compressFiles" :action="compresAction" />

    <FileInput :title="descompressFiles" :buttonText="descompressFiles"  :action="decompresAction"/>
  </div>
</template>

<script setup lang="ts">
import { ref } from "vue";
import FileInput from "../components/FileInput.vue";

const compressFiles: string = "Compactar arquivos";
const descompressFiles: string = "Descompactar arquivos";

const compresAction: string = "compress";
const decompresAction: string = "decompress";

</script>

<style scoped>

</style>
