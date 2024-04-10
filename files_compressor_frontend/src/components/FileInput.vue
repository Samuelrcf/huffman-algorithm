<template>
  <div>
    <v-card color="var(--tertiary-color)" class="mb-12 mt-1">
      <v-card-title class="text-center"
        ><span class="text-h5">{{ props.title }}</span></v-card-title
      >
      <v-container>
        <v-file-input
          v-model="files"
          :show-size="1000"
          color="var(--primary-color)"
          label="Selecione ou arraste arquivos"
          placeholder="Selecione os arquivos"
          prepend-icon="mdi-paperclip"
          variant="outlined"
          counter
          multiple
          bg-color="var(--secondary-color)"
          accept=".txt,.bin"
        >
          <template v-slot:selection="{ fileNames }">
            <template v-for="(fileName, index) in fileNames" :key="fileName">
              <v-chip
                v-if="index < 2"
                class="me-2"
                color="var(--primary-color)"
                size="small"
                label
              >
                {{ fileName }}
              </v-chip>

              <span
                v-else-if="index === 2"
                class="text-overline text-grey-darken-3 mx-2"
              >
                +{{ files.length - 2 }} File(s)
              </span>
            </template>
          </template>
        </v-file-input>

        <div class="text-center">
          <v-btn color="var(--primary-color)" @click="sendFiles">{{
            props.buttonText
          }}</v-btn>
        </div>
      </v-container>
    </v-card>
    <!--<v-card color="var(--tertiary-color)" class="mt-12">
      <v-card-title class="text-center"
        ><span class="text-h5">Download de arquivos</span></v-card-title
      >
      <v-list lines="one">
        <v-list-item v-for="item in allFiles" :key="item">
          <v-card
            color="var(--tertiary-color)"
            class="d-flex align-center mb-1"
          >
            <img
              src="/public/downloadIcon.jpg"
              alt=""
              width="35"
              height="35"
              @click="downloadFile(item)"
              class="cursorPointer"
            />
            <span class="ml-2">{{ item.fileName }}</span>
          </v-card>
        </v-list-item>
      </v-list>
    </v-card>-->
  </div>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref, type PropType } from "vue";
import axios from "axios";
import type FileInfoInterface from "../types/FileInfoInterface";

const props = defineProps({
  title: {
    required: true,
    type: String as PropType<string>,
  },
  buttonText: {
    required: true,
    type: String as PropType<string>,
  },
  action: {
    required: true,
    type: String as PropType<string>,
  },
  findAllCompressedFiles: {
    required: true,
    type: Function as PropType<() => void>,
  },
});

const files = ref([]);

const sendFiles = async () => {
  if (props.action == "compress") {
    let compressedFile = await compressFile();
    console.log("O nome de compressedFile é: " + compressedFile.fileName);
    if (compressedFile.fileName != "") {
      downloadCompressedFile(compressedFile);
      props.findAllCompressedFiles();
    }
  }

  if (props.action == "decompress") {
    let decompressedFile = await decompressFile();
    console.log("O nome de decompressedFile é: " + decompressedFile.fileName);
    if (decompressedFile.fileName != "") {
      downloadDecompressedFile(decompressedFile);
    }
  }
};

const compressFile = async () => {
  const formData = new FormData();
  files.value.forEach((file: any) => {
    formData.append("file", file);
  });
  let compressedFile: FileInfoInterface = {
    fileName: "",
    fileDownloadUri: "",
    fileType: "",
    fileSize: 0,
  };
  try {
    const response = await axios.post(
      "http://localhost:8082/api/file/v1/compressFile",
      formData,
      {
        headers: {
          "Content-Type": "multipart/form-data",
        },
      }
    );
    compressedFile = response.data;
    console.log("Files uploaded successfully:", response.data);
  } catch (error) {
    console.error("Error uploading files:", error);
  } finally {
    return compressedFile;
  }
};

const decompressFile = async () => {
  const formData = new FormData();
  files.value.forEach((file: any) => {
    formData.append("file", file);
  });
  let decompressedFile: FileInfoInterface = {
    fileName: "",
    fileDownloadUri: "",
    fileType: "",
    fileSize: 0,
  };
  try {
    const response = await axios.post(
      "http://localhost:8082/api/file/v1/decompressFile",
      formData,
      {
        headers: {
          "Content-Type": "multipart/form-data",
        },
      }
    );
    decompressedFile = response.data;
    console.log("Files uploaded successfully:", response.data);
  } catch (error) {
    console.error("Error uploading files:", error);
  } finally {
    return decompressedFile;
  }
};

//const allFiles = reactive<any[]>([]);

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

async function downloadDecompressedFile(item: FileInfoInterface) {
  try {
    const response = await axios.get(
      `http://localhost:8082/api/file/v1/downloadDecompressedFile/${item.fileName}`,
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

/*const uploadFiles = async () => {
  const formData = new FormData();
  files.value.forEach((file: any) => {
    formData.append("files", file);
  });

  try {
    const response = await axios.post(
      "http://localhost:8082/api/file/v1/uploadMultipleFiles",
      formData,
      {
        headers: {
          "Content-Type": "multipart/form-data",
        },
      }
    );
    console.log("Files uploaded successfully:", response.data);
  } catch (error) {
    console.error("Error uploading files:", error);
  }
};*/
</script>

<style scoped>
.cursorPointer {
  cursor: pointer;
}
</style>
