import { Outlet, useNavigate } from "react-router-dom";
import { useEffect, useState } from "react";


const ProtectedRoutes = () => {
    const navigate = useNavigate();
    const [ checking, setChecking ] = useState(true);

    useEffect(() => {
        const verifyToken = async() => {
            const token = localStorage.getItem('authToken')

            if (!token) {
                navigate('/', {replace: true})
                return
            }
            try {
                const response = await fetch('/auth/me',{
                    method: 'GET',
                    headers:  {
                            'Authorization':'Bearer ${token}',
                        },
                })
                if (!response.ok) {
                    throw new Error ('Invalid token')
                }
            } catch (error) {
                console.error('Token verification failed: ', error)
                localStorage.removeItem('authToken');
                navigate('/',{ replace: true});
            } finally {
                setChecking(false)
            }
        }
        verifyToken()
    }, [navigate])
    if (checking) return null
    return <Outlet/>
} 

export default ProtectedRoutes