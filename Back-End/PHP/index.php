<?php
// apt install apache2 libapache2-mod-php php php-cli php-json php-mysql
declare(strict_types=1);
// SOAP UI Test Case: [Header -> Authorization] [Value -> Bearer token] Body -> { "externalId": "ID", "type": "FILE", "stage": "DOWNLOAD", "totalPackages": 1, "packageData": "Base64", "manifestUrl": null, "message": "OK" }
// Configuración de encabezados HTTP y CORS
header('Content-Type: application/json; charset=UTF-8');
header('Access-Control-Allow-Origin: *');
header('Access-Control-Allow-Methods: POST, OPTIONS');
header('Access-Control-Allow-Headers: Content-Type, Authorization');
// Respuesta para peticiones Preflight (CORS)
if( $_SERVER['REQUEST_METHOD'] === 'OPTIONS' )
{
    http_response_code(200);
    exit;
}
// 1. Validar que la petición sea POST
if( $_SERVER['REQUEST_METHOD'] !== 'POST' )
{
    $response = ['status' => 'error', 'message' => 'Método no permitido. Usar POST.'];
    jsonResponse($response, 405);
}
// 2. Obtener y validar el token Bearer
$_token = getBearerToken();
$token = 'tu_token_secreto_12345';

if( !$_token || $_token !== $token )
{
    $response = ['status' => 'error', 'message' => 'Token Bearer ausente o inválido.'];
    jsonResponse($response, 401);
}
// 3. Obtener y decodificar la estructura JSON entrante
$inputRaw = file_get_contents('php://input');

if( empty($inputRaw) )
{
    $response = ['status' => 'error', 'message' => 'El cuerpo de la petición(JSON) está vacío.'];
    jsonResponse($response, 400);
}

try
{// PHP 8: Usar JSON_THROW_ON_ERROR para capturar fallos de parseo
    $jdata = json_decode($inputRaw, true, 512, JSON_THROW_ON_ERROR);
}   catch(\JsonException $e)
{
    $response = ['status' => 'error', 'message' => 'Formato JSON inválido: ' . $e->getMessage()];
    jsonResponse($response, 400);
}
// 4. Lógica
if( !isset($jdata['externalId']) || !isset($jdata['type']) || !isset($jdata['stage']) || !isset($jdata['packageData']) )
{
    $response = ['status' => 'error', 'message' => 'Faltan parámetros para la descarga.'];
    jsonResponse($response, 400);
}

if( $jdata['type'] !== "FILE" || $jdata['stage'] !== "DOWNLOAD" )
{
    $response = ['status' => 'error', 'message' => 'No es posible realizar la descarga.'];
    jsonResponse($response, 400);
}
// Validar si el Base64 Contiene Encabezado(data:image/png;base64,)
$filename = '/var/www/html/flsw/' . $jdata['externalId'] . '.zip';
$bytes = base64_decode($jdata['packageData'], true);
// Validar Owner User Directory: www-data
if( $bytes === false )
{
    $response = ['status' => 'error', 'message' => 'Cadena Inválida'];
    jsonResponse($response, 400);
}

$result = file_put_contents($filename, $bytes);

if( $result === false )
{
    $error = error_get_last();
    $iuser = posix_getpwuid(posix_getuid());
    $response = ['status' => 'error', 'message' => 'Error al guardar archivo: ' . $filename . ' ' . $iuser['gid'] . ' ' . $iuser['uid'] . ' ' . $iuser['name'] . ' ' . ($error['message'] ?? 'Error desconocido')];
    jsonResponse($response, 400);
}

$hostname = '127.0.0.1';
$username = 'portaluser';
$password = 'portaluser';
$database = 'portalcfdi';

$db = new mysqli($hostname, $username, $password, $database);

if( $db->connect_error )
{
    $response = ['status' => 'error', 'message' => $db->connect_error];
    jsonResponse($response, 400);
}

$db->set_charset("utf8mb4");
$db->close();

$response = ['status' => 'success', 'message' => 'OK', 'timestamp' => time()];
jsonResponse($response, 200);

/* --- Funciones Auxiliares --- */

/**
 * Extrae el token Bearer de las cabeceras HTTP de la petición.
 */
function getBearerToken(): ? string
{
    $headers = null;

    if( isset($_SERVER['Authorization']) )
    {
        $headers = trim($_SERVER['Authorization']);
    }   elseif( isset($_SERVER['HTTP_AUTHORIZATION']) )
    {// Capturado vía .htaccess en Apache
        $headers = trim($_SERVER['HTTP_AUTHORIZATION']);
    }   elseif( function_exists('apache_request_headers') )
    {
        $requestHeaders = apache_request_headers();
        // Normalizar claves a minúsculas
        $requestHeaders = array_combine(array_map('ucwords', array_keys($requestHeaders)), array_values($requestHeaders));
        if( isset( $requestHeaders['Authorization']) )
        {
            $headers = trim($requestHeaders['Authorization']);
        }
    }

    if(!empty($headers) && preg_match('/Bearer\s(\S+)/i', $headers, $matches))
    {
        return $matches[1];
    }

    return null;
}
/**
 * Retorna la respuesta en formato JSON y asigna el código de estado HTTP.
 */
function jsonResponse(array $data, int $code = 200): void
{
    http_response_code($code);
    echo json_encode($data, JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT);
    exit;
}
?>
