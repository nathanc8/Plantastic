// to access the HTTP methods that we want to handle

import { http, HttpResponse } from "msw";
import { z } from "zod";

export interface User {
  id: number;
  pseudo: string;
  email: string;
  password: string;
}

const Userschema = z.object({
  pseudo: z.string(),
  email: z.string(),
  password: z.string(),
});

export const handlers = [
  http.get<never, User[]>("/api/auth/login", (_request) => {
    return HttpResponse.json([
      {
        id: 1,
        pseudo: "toto",
        email: "toto@yopmail.fr",
        password: "User1234!",
      },
    ]);
  }),

  http.post<never, User[]>("/api/auth/signup", async ({ request }) => {
    const requestBody = await request.json();
    const result = Userschema.safeParse(requestBody);
    console.debug("How's the body? ", result);
    if (!result.success) {
      return HttpResponse.json({ error: "Invalid input" }, { status: 400 });
    }
    const parsed = result.data;
    console.debug(parsed);

    // ✅ Simuler un ID pour le nouvel utilisateur
    const newUser: User = {
      id: 2, // tu peux faire un ID dynamique si tu veux
      ...parsed,
    };

    console.debug("User créé :", newUser);

    return HttpResponse.json([newUser], { status: 201 });
  }),
];
