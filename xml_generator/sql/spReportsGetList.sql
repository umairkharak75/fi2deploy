IF EXISTS(select * from INFORMATION_SCHEMA.ROUTINES where ROUTINE_NAME = 'spReportsGetList')
BEGIN
	drop procedure dbo.spReportsGetList
END
GO

--------------------------------------------------------------------------------------------
-- Get data at specific condition from dbo.Reports
-- History:
-- 03.02.2019 Jure; created
--------------------------------------------------------------------------------------------
CREATE PROCEDURE [dbo].[spReportsGetList]
	@search_query varchar(max),
	@page_number int = 1,
	@rows_per_page int = 30
AS
BEGIN

	-- EDM Data contract
	if (1=2) begin
		select 
			Reports.id as id,
			Reports.shema_naziv as shema_naziv,
			Reports.naziv_zascita as naziv_zascita,
			Reports.naziv_proizvod as naziv_proizvod,
			Reports.vrednost as vrednost,
			Reports.enota as enota,
			Reports.email as email
		from 
			dbo.Reports
			
		WHERE 
			1=2 
	end

	declare @cmd varchar(8000) = ' 
		select 
			Reports.id as id,
			Reports.shema_naziv as shema_naziv,
			Reports.naziv_zascita as naziv_zascita,
			Reports.naziv_proizvod as naziv_proizvod,
			Reports.vrednost as vrednost,
			Reports.enota as enota,
			Reports.email as email
		from 
			dbo.Reports
			
	'

	if isnull(@search_query, '') != '' begin
		set @cmd = @cmd + 'where '
			set @cmd = @cmd + replace('Reports.shema_naziv like ''%[0]%'' OR ', '[0]', @search_query)
			set @cmd = @cmd + replace('Reports.naziv_zascita like ''%[0]%'' OR ', '[0]', @search_query)
			set @cmd = @cmd + replace('Reports.naziv_proizvod like ''%[0]%'' OR ', '[0]', @search_query)
			set @cmd = @cmd + replace('Reports.enota like ''%[0]%'' OR ', '[0]', @search_query)
			set @cmd = @cmd + replace('Reports.email like ''%[0]%''', '[0]', @search_query)
	end

	set @cmd = @cmd + '
	ORDER BY 
			Reports.id
	OFFSET ((@page_number - 1) * @rows_per_page) ROWS
	FETCH NEXT @rows_per_page ROWS ONLY;'
	
	set @cmd = REPLACE(@cmd, '@page_number', @page_number)
	set @cmd = REPLACE(@cmd, '@rows_per_page', @rows_per_page)
	
	print(@cmd)
	exec(@cmd)
END

