import {
  $,
  addTask,
  api,
  documentReady,
  handleApiError,
  lock,
  resetInput,
  unlock,
} from "./common.js";

const required = ["email", "password"];
const formIds = [...required, "submit"];
const regex = {
  email: /^[\w+-.*]+@[\w-]+\.[\w-.]+$/,
  password:
    /^(?=.*[a-zA-Z])(?=.*\d)(?=.*[!@#$%^&*()\-+=`~])[\w!@#$%^&*()\-+=`~]{8,}$/,
};

const init = (key) => ({
  key: key,
  value: $(`#${key}`).value,
  regex: regex[key],
});

const check = (data) => data.regex.test(data.value);

const assign = (acc, data) => {
  acc[data.key] = data.value;
  return acc;
};

const cleanUp = () => {
  required.forEach(resetInput);
  formIds.forEach(unlock);
};

const signIn = async (formData) => {
  await api.post("/v1/admins/accounts/sign-in", { json: formData }).json();
};

const handleFormSubmit = async (event) => {
  event.preventDefault();

  formIds.forEach(lock);

  const formData = required.map(init).filter(check).reduce(assign, {});

  if (Object.keys(formData).length !== required.length) {
    cleanUp();
    alert("이메일 및 비밀번호를 확인해주세요.");
    return;
  }

  try {
    await signIn(formData);
    globalThis.location.href = "/admins";
  } catch (error) {
    await handleApiError(error);
  } finally {
    cleanUp();
  }
};

addTask(() => $("#form").addEventListener("submit", handleFormSubmit));

document.addEventListener("DOMContentLoaded", documentReady);
