import { Stack, Typography } from '@mui/material'

type SectionHeaderProps = {
  overline?: string
  title: string
  description: string
  align?: 'left' | 'center'
}

export function SectionHeader({
  overline,
  title,
  description,
  align = 'left',
}: SectionHeaderProps) {
  return (
    <Stack
      spacing={2}
      sx={{
        mb: 6,
        maxWidth: align === 'center' ? 720 : 640,
        mx: align === 'center' ? 'auto' : undefined,
        textAlign: align,
      }}
    >
      {overline && (
        <Typography
          variant="overline"
          sx={{ color: 'primary.main', fontWeight: 700, letterSpacing: '0.12em' }}
        >
          {overline}
        </Typography>
      )}
      <Typography variant="h3" sx={{ fontWeight: 800, letterSpacing: '-0.02em' }}>
        {title}
      </Typography>
      <Typography variant="h6" color="text.secondary" sx={{ fontWeight: 400, lineHeight: 1.7 }}>
        {description}
      </Typography>
    </Stack>
  )
}
