IF EXISTS(select * from INFORMATION_SCHEMA.ROUTINES where ROUTINE_NAME = 'spPrilogeProizvodovReportGetList')
BEGIN
	drop procedure dbo.spPrilogeProizvodovReportGetList
END
GO

--------------------------------------------------------------------------------------------
-- Get data at specific condition from dbo.PRILOGEPROIZVODOVREPORT
-- History:
-- 03.02.2019 Jure; created
--------------------------------------------------------------------------------------------
CREATE PROCEDURE [dbo].[spPrilogeProizvodovReportGetList]
	@search_query varchar(max),
	@page_number int = 1,
	@rows_per_page int = 30
AS
BEGIN

	-- EDM Data contract
	if (1=2) begin
		select 
			PRILOGEPROIZVODOVREPORT.id as id,
			PRILOGEPROIZVODOVREPORT.kmgmid_imetnika as kmgmid_imetnika,
			PRILOGEPROIZVODOVREPORT.naziv_imetnika as naziv_imetnika,
			PRILOGEPROIZVODOVREPORT.stevilka_certifikata as stevilka_certifikata,
			PRILOGEPROIZVODOVREPORT.shema as shema,
			PRILOGEPROIZVODOVREPORT.naziv_zascita as naziv_zascita,
			PRILOGEPROIZVODOVREPORT.naziv_proizvod as naziv_proizvod,
			PRILOGEPROIZVODOVREPORT.stevilka as stevilka,
			PRILOGEPROIZVODOVREPORT.dat_izdaje as dat_izdaje,
			PRILOGEPROIZVODOVREPORT.dat_velj as dat_velj,
			PRILOGEPROIZVODOVREPORT.status as status,
			PRILOGEPROIZVODOVREPORT.vsebina as vsebina
		from 
			dbo.PRILOGEPROIZVODOVREPORT
			
		WHERE 
			1=2 
	end

	declare @cmd varchar(8000) = ' 
		select 
			PRILOGEPROIZVODOVREPORT.id as id,
			PRILOGEPROIZVODOVREPORT.kmgmid_imetnika as kmgmid_imetnika,
			PRILOGEPROIZVODOVREPORT.naziv_imetnika as naziv_imetnika,
			PRILOGEPROIZVODOVREPORT.stevilka_certifikata as stevilka_certifikata,
			PRILOGEPROIZVODOVREPORT.shema as shema,
			PRILOGEPROIZVODOVREPORT.naziv_zascita as naziv_zascita,
			PRILOGEPROIZVODOVREPORT.naziv_proizvod as naziv_proizvod,
			PRILOGEPROIZVODOVREPORT.stevilka as stevilka,
			PRILOGEPROIZVODOVREPORT.dat_izdaje as dat_izdaje,
			PRILOGEPROIZVODOVREPORT.dat_velj as dat_velj,
			PRILOGEPROIZVODOVREPORT.status as status,
			PRILOGEPROIZVODOVREPORT.vsebina as vsebina
		from 
			dbo.PRILOGEPROIZVODOVREPORT
			
	'

	if isnull(@search_query, '') != '' begin
		set @cmd = @cmd + 'where '
			set @cmd = @cmd + replace('PRILOGEPROIZVODOVREPORT.kmgmid_imetnika like ''%[0]%'' OR ', '[0]', @search_query)
			set @cmd = @cmd + replace('PRILOGEPROIZVODOVREPORT.naziv_imetnika like ''%[0]%'' OR ', '[0]', @search_query)
			set @cmd = @cmd + replace('PRILOGEPROIZVODOVREPORT.stevilka_certifikata like ''%[0]%'' OR ', '[0]', @search_query)
			set @cmd = @cmd + replace('PRILOGEPROIZVODOVREPORT.shema like ''%[0]%'' OR ', '[0]', @search_query)
			set @cmd = @cmd + replace('PRILOGEPROIZVODOVREPORT.naziv_zascita like ''%[0]%'' OR ', '[0]', @search_query)
			set @cmd = @cmd + replace('PRILOGEPROIZVODOVREPORT.naziv_proizvod like ''%[0]%'' OR ', '[0]', @search_query)
			set @cmd = @cmd + replace('PRILOGEPROIZVODOVREPORT.stevilka like ''%[0]%'' OR ', '[0]', @search_query)
			set @cmd = @cmd + replace('PRILOGEPROIZVODOVREPORT.status like ''%[0]%'' OR ', '[0]', @search_query)
			set @cmd = @cmd + replace('PRILOGEPROIZVODOVREPORT.vsebina like ''%[0]%''', '[0]', @search_query)
	end

	set @cmd = @cmd + '
	ORDER BY 
			PRILOGEPROIZVODOVREPORT.id
	OFFSET ((@page_number - 1) * @rows_per_page) ROWS
	FETCH NEXT @rows_per_page ROWS ONLY;'
	
	set @cmd = REPLACE(@cmd, '@page_number', @page_number)
	set @cmd = REPLACE(@cmd, '@rows_per_page', @rows_per_page)
	
	print(@cmd)
	exec(@cmd)
END

