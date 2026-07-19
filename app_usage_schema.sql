-- ============================================================================
-- FamilyHub app_usage table
-- Tracks per-device app usage reported by CHILD family members and viewed by
-- PARENTS in the same family group.
-- ============================================================================

CREATE TABLE IF NOT EXISTS app_usage (
    id                TEXT PRIMARY KEY,
    family_member_id  TEXT NOT NULL REFERENCES family_members(id) ON DELETE CASCADE,
    family_group_id   TEXT NOT NULL REFERENCES family_groups(id) ON DELETE CASCADE,
    package_name      TEXT NOT NULL,
    app_name          TEXT NOT NULL,
    usage_date        TEXT NOT NULL,           -- "YYYY-MM-DD"
    total_time_ms     BIGINT NOT NULL DEFAULT 0,
    last_time_used    BIGINT NOT NULL DEFAULT 0,
    synced_at         BIGINT NOT NULL DEFAULT (EXTRACT(EPOCH FROM NOW()) * 1000)::BIGINT,
    UNIQUE (family_member_id, package_name, usage_date)
);

ALTER TABLE app_usage ENABLE ROW LEVEL SECURITY;

-- Only members of the same family group can read app usage.
-- (Parents see all children; the UI scopes a child to their own data.)
CREATE POLICY "app_usage_select_group"
    ON app_usage FOR SELECT
    USING (family_group_id = public.current_user_family_group_id());

-- A member may only insert/update rows linked to their own family_member
-- records. The app syncs from the child's device using the child's member id.
CREATE POLICY "app_usage_insert_self"
    ON app_usage FOR INSERT
    WITH CHECK (
        family_member_id IN (
            SELECT id FROM family_members WHERE user_id = auth.uid()
        )
        AND family_group_id = public.current_user_family_group_id()
    );

CREATE POLICY "app_usage_update_self"
    ON app_usage FOR UPDATE
    USING (
        family_member_id IN (
            SELECT id FROM family_members WHERE user_id = auth.uid()
        )
        AND family_group_id = public.current_user_family_group_id()
    )
    WITH CHECK (family_group_id = public.current_user_family_group_id());

CREATE POLICY "app_usage_delete_self"
    ON app_usage FOR DELETE
    USING (
        family_member_id IN (
            SELECT id FROM family_members WHERE user_id = auth.uid()
        )
        AND family_group_id = public.current_user_family_group_id()
    );

-- ============================================================================
-- Indexes
-- ============================================================================

CREATE INDEX IF NOT EXISTS idx_app_usage_group_member_date
    ON app_usage(family_group_id, family_member_id, usage_date);
CREATE INDEX IF NOT EXISTS idx_app_usage_package
    ON app_usage(package_name);

-- ============================================================================
-- IMPORTANT NOTES
-- ============================================================================
-- 1. The client upserts on conflict (family_member_id, package_name, usage_date)
--    so re-running sync does not create duplicate rows.
-- 2. The `id` column is generated client-side as:
--    "${family_member_id}_${package_name}_${usage_date}".
-- 3. RLS relies on the authenticated user having a family_members row linked to
--    auth.uid(). Do not filter by family_group_id on the client for security.
-- ============================================================================
