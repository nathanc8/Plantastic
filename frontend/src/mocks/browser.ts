// création et configuration du worker
import { setupWorker } from 'msw/browser';
import { handlers } from './handlers';


export const worker = setupWorker(...handlers);