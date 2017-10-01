export const formatCantidad = (cantidad) =>
    cantidad.toLocaleString('es-AR', {style: 'currency', currency: 'ARS', minimumFractionDigits: 2});
