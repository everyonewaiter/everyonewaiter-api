import ky, { isHTTPError } from "https://cdn.jsdelivr.net/npm/ky/+esm";

const api = ky.create({
  hooks: {
    afterResponse: [
      (request, options, response) => {
        if (response.status === 401 || response.status === 403) {
          globalThis.location.href = "/admins/sign-in";
        }
      },
    ],
  },
});

const handleApiError = async (error) => {
  let message = error.message;
  if (isHTTPError(error)) {
    const res = await error.response.json();
    message = res.message;
  }
  alert(message);
};

const tasks = [];

const runTask = (task) => task();

const addTask = (task) => tasks.push(task);

const $ = (selector) => {
  if (typeof selector !== "string") {
    throw new TypeError("Selector must be a string");
  }

  if (selector.startsWith("#")) {
    return document.querySelector(selector);
  }
  return document.querySelectorAll(selector);
};

const lock = (id) => {
  $(`#${id}`).setAttribute("disabled", "true");
};

const unlock = (id) => {
  $(`#${id}`).removeAttribute("disabled");
};

const resetInput = (id) => {
  $(`#${id}`).value = "";
};

const documentReady = () => {
  tasks.forEach(runTask);
};

export {
  api,
  handleApiError,
  $,
  addTask,
  lock,
  unlock,
  resetInput,
  documentReady,
};
