export function hasPermission(
  userRoles: string[] | undefined,
  allowedRoles: string[],
): boolean {
  if (!userRoles?.length) {
    return false
  }

  return allowedRoles.some((role) => userRoles.includes(role))
}

export function hasAllPermissions(
  userRoles: string[] | undefined,
  requiredRoles: string[],
): boolean {
  if (!userRoles?.length) {
    return false
  }

  return requiredRoles.every((role) => userRoles.includes(role))
}
