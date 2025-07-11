// // to access the HTTP methods that we want to handle

// import { http, HttpResponse } from 'msw';

// interface User {
//     id: number;
//     pseudo: string;
//     email: string;
//     password: string;
// }

// export const handlers = [
//     http.get<never,User[]>("/api/auth/login", ({request}) => {
//         return HttpResponse.json([
//             {
//                 id: 1, 
//                 pseudo: "toto",
//                 email: "toto@yopmail.fr",
//                 password: "User1234!"
//             },
//         ]);
//     }),
// ];

