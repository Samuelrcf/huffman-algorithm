<template>
  <div>
    <v-tabs v-model="tab" bg-color="var(--primary-color)" class="mb-5 rounded">
      <v-tab value="one">Compactar</v-tab>
      <v-tab value="two">Descompactar</v-tab>
    </v-tabs>
    <v-window v-model="tab">
      <v-window-item value="one">
        <FileInput
          :title="compressFiles"
          :buttonText="compressFiles"
          :action="compressAction"
          :findAllCompressedFiles="findAllCompressedFiles"
        />
      </v-window-item>

      <v-window-item value="two">
        <FileInput
          :title="descompressFiles"
          :buttonText="descompressFiles"
          :action="decompressAction"
          :findAllCompressedFiles="findAllCompressedFiles"
        />
      </v-window-item>
    </v-window>

    <v-card color="var(--tertiary-color)" class="mt-12">
      <v-card-title class="text-center"
        ><span class="text-h5">Download de arquivos</span></v-card-title
      >
      <v-list lines="one">
        <v-list-item v-for="item in allFiles" :key="item">
          <div color="var(--secondary-color)" class="d-flex align-center mb-1">
            <div class="iconContainer mr-5">
              <img
                src="/downloadIcon3.svg"
                alt=""
                @click="downloadCompressedFile(item)"
                class="cursorPointer ml-2 iconSize"
              />
            </div>
            <div class="iconContainer mr-2">
              <img
                src="/removeIcon.svg"
                alt=""
                @click="deleteCompressedFile(item)"
                class="cursorPointer iconSize"
              />
            </div>

            <span class="ml-2">{{ item.fileName }}</span>
          </div>
          <v-divider />
        </v-list-item>
      </v-list>
    </v-card>
  </div>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref } from "vue";
import FileInput from "../components/FileInput.vue";
import axios from "axios";
import type FileInfoInterface from "@/types/FileInfoInterface";

const compressFiles: string = "Compactar arquivos";
const descompressFiles: string = "Descompactar arquivos";

const compressAction: string = "compress";
const decompressAction: string = "decompress";

onMounted(() => {
  //findAllFiles();
  findAllCompressedFiles();
});

const allFiles = reactive<any[]>([]);
const tab = ref<string>("");

/*const findAllFiles = async () => {
  try {
    const response = await axios.get(
      "http://localhost:8082/api/file/v1/findAllFiles",
      {
        headers: {
          "Content-Type": "application/json",
        },
      }
    );
    console.log(response.data[0]);
    allFiles.push(...response.data);
  } catch (error) {
    console.error("Error:", error);
  }
};*/

const findAllCompressedFiles = async () => {
  try {
    const response = await axios.get(
      "http://localhost:8082/api/file/v1/findAllCompressedFiles",
      {
        headers: {
          "Content-Type": "application/json",
        },
      }
    );
    console.log(response.data[0]);
    allFiles.splice(0);
    allFiles.push(...response.data);
  } catch (error) {
    console.error("Error:", error);
  }
};

async function downloadFile(item: any) {
  try {
    const response = await axios.get(
      `http://localhost:8082/api/file/v1/downloadFile/${item.fileName}`,
      {
        responseType: "blob", // Define o tipo de resposta como blob
      }
    );

    // Cria um link temporário para o arquivo
    const url = window.URL.createObjectURL(new Blob([response.data]));
    const link = document.createElement("a");
    link.href = url;
    link.setAttribute("download", "arquivoBinCompactado.txt"); // Nome do arquivo para download
    //link.setAttribute('download', "arquivoEmPdf.pdf");
    document.body.appendChild(link);
    link.click();
    // Limpa o objeto URL criado
    window.URL.revokeObjectURL(url);
  } catch (error) {
    console.error("Erro ao baixar o arquivo:", error);
  }
}

async function downloadCompressedFile(item: FileInfoInterface) {
  try {
    const response = await axios.get(
      `http://localhost:8082/api/file/v1/downloadCompressedFile/${item.fileName}`,
      {
        responseType: "blob",
      }
    );

    // Cria um link temporário para o arquivo
    const url = window.URL.createObjectURL(new Blob([response.data]));
    const link = document.createElement("a");
    link.href = url;
    link.setAttribute("download", item.fileName); // Nome do arquivo para download
    //link.setAttribute('download', "arquivoEmPdf.pdf");
    document.body.appendChild(link);
    link.click();
    // Limpa o objeto URL criado
    window.URL.revokeObjectURL(url);
  } catch (error) {
    console.error("Erro ao baixar o arquivo:", error);
  }
}

async function deleteCompressedFile(item: FileInfoInterface) {
  try {
    const response = await axios.delete(
      `http://localhost:8082/api/file/v1/${item.fileName}`,
      {
        headers: {
          "Content-Type": "application/json",
        },
      }
    );
    if (response.status === 204) {
      // Filtra os arquivos, removendo o arquivo excluído
      /*let newFilesList = allFiles.filter(
        (file) => file.fileName !== item.fileName
      );

      allFiles.splice(0);
      newFilesList.forEach((file) => allFiles.push(file));*/
      findAllCompressedFiles();
    }
  } catch (error) {
    console.error("Error:", error);
  }
}
</script>

<style scoped>
.cursorPointer {
  cursor: pointer;
}

.iconContainer {
  width: 35px;
  height: 35px;
}

.iconSize {
  width: 35px;
  height: 35px;
  background-color: transparent;
  transition: background-color 0.3s;
}

.iconSize:hover {
  background-color: var(--primary-color);
}
</style>
